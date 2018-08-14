package org.memehazard.wheel.asset.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.memehazard.wheel.asset.model.Asset3DStatistics;

public class TestData
{

    public static final int[]             ASSET_ASSETSET_IDS     = { 1, 1, 1, 1, 2, 2 };
    public static final int[]             ASSET_ENTITY_IDS       = { 1000, 1001, 1002, 1003, 1004, 1005 };
    public static final int[]             ASSET_IDS              = { 1, 2, 3, 4, 5, 6 };
    public static final String[]          ASSET_NAMES            = { "Asset 50", "Asset 41", "Asset 32", "Asset 23", "Asset 14", "Asset 05" };
    public static final String            ASSET_OBJ_FILENAME_ALL = "file.obj";
    public static final Asset3DStatistics ASSET_STATS_ALL        = new Asset3DStatistics(
                                                                         new double[] { 0.01, 0.02, 0.03 },
                                                                         new double[] { 0.11, 0.12, 0.13 },
                                                                         new double[] { 0.21, 0.22, 0.23 });
    public static final Asset3DStatistics ASSET_STATS_ALT        = new Asset3DStatistics(
                                                                         new double[] { 11.1, 11.2, 11.3 },
                                                                         new double[] { 12.1, 12.2, 12.3 },
                                                                         new double[] { 13.1, 13.2, 13.3 });
    public static final List<String>      ASSET_STYLE_TAGS       = initStyleTags(false);
    public static final List<String>      ASSET_STYLE_TAGS_ALT   = initStyleTags(true);
    public static final String            ASSET_X3D_FILENAME_ALL = "file.x3d";

    public static final int[]             SET_IDS                = { 1, 2, 3, 4 };
    public static final String            SET_MAINTAINER_ALL     = "Maintainer";
    public static final String[]          SET_NAMES              = { "Asset Set 30", "Asset Set 21", "Asset Set 12", "Asset Set 03" };


    private static List<String> initStyleTags(boolean alt)
    {
        List<String> tags = new ArrayList<String>();
        tags.add("asset");
        tags.add("style");
        tags.add("tags");
        if (alt)
            tags.add("alt");

        Collections.sort(tags);

        return tags;
    }

}
