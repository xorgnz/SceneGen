/**
 * 
 */
package org.memehazard.wheel.asset.codec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.memehazard.math.geometry.Face3;
import org.memehazard.math.geometry.Point3D;
import org.memehazard.wheel.asset.model.Mesh;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjCodec
{
    private Logger log = LoggerFactory.getLogger(X3DCodec.class);


    public void encode(File f, Mesh mesh) throws IOException, CodecException
    {
        FileWriter fw = new FileWriter(f);

        try
        {
            fw.write("o Generated_Mesh_From_X3D" + IOUtils.LINE_SEPARATOR);

            for (Point3D p : mesh.getVertexData())
                fw.write(String.format("v %.6f %.6f %.6f", p.x, p.y, p.z) + IOUtils.LINE_SEPARATOR);

            for (Face3 face : mesh.getFaceData())
            {
                fw.write(String.format("f %d %d %d", face.idx0 + 1, face.idx1 + 1, face.idx2 + 1) + IOUtils.LINE_SEPARATOR);
            }
        }
        finally
        {
            fw.close();
        }
    }


    public Mesh decode(File f) throws IOException, CodecException
    {
        BufferedReader br = new BufferedReader(new FileReader(f));

        try
        {
            Mesh mesh = new Mesh();

            // Ensure that we're working on a single component OBJ file
            String line;
            boolean foundOHeader = false;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith("o "))
                {
                    if (foundOHeader)
                        throw new CodecException("OBJ file contains multiple components. Second found on line " + line);
                    foundOHeader = true;
                }
            }
            br.close();

            // Read lines into the data structure.
            br = new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null)
            {
                String[] lineChunks = StringUtils.split(line.substring(2), " ");

                // Vertex line
                if (line.startsWith("v "))
                {
                    double x = Double.parseDouble(lineChunks[0]);
                    double y = Double.parseDouble(lineChunks[1]);
                    double z = Double.parseDouble(lineChunks[2]);

                    mesh.addVertex(x, y, z);
                }

                // Face line
                if (line.startsWith("f "))
                {
                    // Parse out index values from the string chunks of a face line
                    int[] indices = new int[lineChunks.length];
                    for (int i = 0; i < lineChunks.length; i++)
                    {
                        if (StringUtils.contains(lineChunks[i], "/"))
                            indices[i] = Integer.parseInt(StringUtils.split(lineChunks[i], "/")[0]) - 1;

                        else
                            indices[i] = Integer.parseInt(lineChunks[i]) - 1;
                    }

                    // Barf if face has too few or too many vertices
                    if (indices.length < 3 || indices.length > 4)
                        throw new CodecException("OBj file contains faces that are not triangles or quads");

                    // 3-Face
                    else if (indices.length == 3)
                        mesh.addFace(indices[0], indices[1], indices[2]);

                    // 4-Face
                    else if (indices.length == 4)
                    {
                        log.info("4Face - "
                                 + String.format("%d %d %d %d", indices[0], indices[1], indices[2], indices[3]));
                        log.info("   to - " + String.format("%d %d %d", indices[0], indices[1], indices[2]));
                        log.info("   to - " + String.format("%d %d %d", indices[0], indices[2], indices[3]));

                        mesh.addFacesFromQuad(indices[0], indices[1], indices[2], indices[3]);
                    }
                }
            }

            return mesh;
        }
        catch (IOException ioe)
        {
            throw ioe;
        }
        catch (CodecException ce)
        {
            throw ce;
        }
        finally
        {
            br.close();
        }
    }
}