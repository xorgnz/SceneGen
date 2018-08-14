package org.memehazard.wheel.asset.utils;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.memehazard.math.geometry.Face3;
import org.memehazard.math.geometry.Point3D;
import org.memehazard.wheel.asset.codec.ObjCodec;
import org.memehazard.wheel.asset.model.Mesh;

public class SwitchYZAxesUtility
{
    private static final String DIR_FILES_IN  = "D:\\work\\3D_workspace\\BodyParts3D\\bp3d_set_all\\all";
    private static final String DIR_FILES_OUT = "D:\\work\\3D_workspace\\BodyParts3D\\bp3d_set_all\\all_modified";


    public static void main(String[] argv) throws Exception
    {
        File f_dir_in = new File(DIR_FILES_IN);
        File f_dir_out = new File(DIR_FILES_OUT);

        ObjCodec codec = new ObjCodec();

        File[] files = f_dir_in.listFiles();

        for (File f_in : files)
        {
            if (FilenameUtils.getExtension(f_in.getName()).equals("obj"))
            {
                Mesh m_in = codec.decode(f_in);
                Mesh m_out = new Mesh();

                // Copy faces 
                for (Face3 face : m_in.getFaceData())
                    m_out.addFace(face);

                // Copy vertices, switching Y and Z axes
                for (Point3D p : m_in.getVertexData())
                    m_out.addVertex(p.x, p.z, -p.y);

                File f_out = new File(f_dir_out, f_in.getName());

                codec.encode(f_out, m_out);

                System.err.println("Finished " + f_out.getAbsolutePath());
            }
            else
            {
                System.err.println("Skipping " + f_in.getAbsolutePath());
            }
        }
    }
}
