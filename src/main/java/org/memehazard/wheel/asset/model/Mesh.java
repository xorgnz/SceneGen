/**
 * 
 */
package org.memehazard.wheel.asset.model;

import java.util.ArrayList;
import java.util.List;

import org.memehazard.math.geometry.Face3;
import org.memehazard.math.geometry.Point3D;


public class Mesh
{
    private List<Face3>   faceData   = new ArrayList<Face3>();
    private List<Point3D> vertexData = new ArrayList<Point3D>();


    public void addFace(Face3 face)
    {
        this.faceData.add(face);
    }


    public void addFace(int idx0, int idx1, int idx2)
    {
        this.faceData.add(new Face3(idx0, idx1, idx2));
    }


    public void addFacesFromQuad(int idx0, int idx1, int idx2, int idx3)
    {
        // System.err.println("adding from quad " + idx0 + " " + idx1 + " " + idx2 + " " + idx3);
        this.faceData.add(new Face3(idx0, idx1, idx2));
        this.faceData.add(new Face3(idx0, idx2, idx3));
    }


    public void addVertex(double x, double y, double z)
    {
        this.vertexData.add(new Point3D(x, y, z));
    }


    public void addVertex(Point3D vertex)
    {
        this.vertexData.add(vertex);
    }


    public Asset3DStatistics calculateStatistics()
    {
        // Create data structure
        Asset3DStatistics stats = new Asset3DStatistics();

        for (Point3D p : vertexData)
        {
            // Update statistics.
            stats.centroid_x += p.x;
            stats.centroid_y += p.y;
            stats.centroid_z += p.z;

            stats.min_x = p.x < stats.min_x ? p.x : stats.min_x;
            stats.min_y = p.y < stats.min_y ? p.y : stats.min_y;
            stats.min_z = p.z < stats.min_z ? p.z : stats.min_z;

            stats.max_x = p.x > stats.max_x ? p.x : stats.max_x;
            stats.max_y = p.y > stats.max_y ? p.y : stats.max_y;
            stats.max_z = p.z > stats.max_z ? p.z : stats.max_z;
        }

        // Finish calculating centroid;
        stats.centroid_x /= vertexData.size();
        stats.centroid_y /= vertexData.size();
        stats.centroid_z /= vertexData.size();

        return stats;
    }


    /**
     * @return the faceData
     */
    public List<Face3> getFaceData()
    {
        return faceData;
    }


    /**
     * @return the vertexData
     */
    public List<Point3D> getVertexData()
    {
        return vertexData;
    }


    /**
     * 
     */
    public boolean isValid()
    {
        if (vertexData.size() == 0)
            return false;

        if (faceData.size() == 0)
            return false;

        int vSize = vertexData.size();
        for (Face3 face : faceData)
        {
            if (face.idx0 >= vSize || face.idx1 >= vSize || face.idx2 >= vSize)
                return false;
        }

        return true;
    }
}