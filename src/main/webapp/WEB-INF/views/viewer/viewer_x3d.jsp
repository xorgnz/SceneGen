<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns="http://www.w3.org/1999/xhtml" version="2.0">
<X3D profile="Immersive" version="3.2" xmlns:xsd="http://www.w3.org/2001/XMLSchema-instance" xmlns:x3d="http://www.web3d.org/specifications/x3d" xsd:noNamespaceSchemaLocation="http://www.web3d.org/specifications/x3d-3.2.xsd">
<Scene>
    <NavigationInfo headlight="true" type="&amp;quot;NONE&amp;quot;" />

    <!--
        Calculate necessary camera distance based on scene extent.
        2.8 multiplier is chosen as being slightly more than 1/tan(pi/8) (the half view-angle of the viewpoint)
     -->
    <c:set var="z_pos" scope="page" value="${2.8 * extent}" />
    <c:url var="base_url" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

    <!-- Viewpoint -->
    <Viewpoint position="0 0 ${z_pos}" />

    <!-- Primary scene contents -->
    <Transform DEF="T_Zoom" scale="1 1 1">
        <Transform DEF="T_ViewpointRotation">
            <Transform DEF="T_ViewpointCenter">

                <!-- Re-center scene models around overall scene centroid -->
                <Transform translation="${-scnd.centroid[0]} ${-scnd.centroid[1]} ${-scnd.centroid[2]}">
                <c:if test="${not empty entities}">
                    <c:forEach var="scnod" items="${scnd.sceneObjectDescriptors}">
                        <Switch DEF="Sw_${scnod.entity.id}" whichChoice="1">
                        <Group />
                        <Group>
                        <TouchSensor DEF="TS_${scnod.entity.id}" />
                        <Inline DEF="Inline_${scnod.entity.id}" load="true" url="&amp;quot;${base_url}asset/file/${scnod.x3dFilename}&amp;quot;" />
                        <IMPORT AS="Mtl_${scnod.entity.id}" exportedDEF="Material" inlineDEF="Inline_52735" />
                        </Group>
                        </Switch>
                    </c:forEach>
                </c:if>
                </Transform>

                <!-- Landmark - center box (for solving rotation issues) -->
                <!--
                <Shape>
                    <Box size="1 1 1" />
                    <Appearance>
                        <Material diffuseColor="0 1 0" />
                    </Appearance>
                </Shape>
                -->
            </Transform>
        </Transform>
    </Transform>

    <!-- Background field (dark green) -->
    <Transform translation="0 0 -200">
        <TouchSensor DEF="TS_Background" />
        <Shape>
            <Box size="10000.0 10000.0 1.0" />
            <Appearance>
                <Material diffuseColor="0 0.1 0" />
            </Appearance>
        </Shape>
    </Transform>

    <!-- HUD - Frame -->
    <Transform translation="0 -1.9 ${z_pos - 0.98}">
        <Shape>
            <Box size="10.0 3.0 0.001" />
            <Appearance>
                <Material diffuseColor="1 1 1" />
            </Appearance>
        </Shape>
    </Transform>
    <Transform translation="0 1.9 ${z_pos - 0.98}">
        <Shape>
            <Box size="10.0 3.0 0.001" />
            <Appearance>
                <Material diffuseColor="1 1 1" />
            </Appearance>
        </Shape>
    </Transform>
    <Transform translation="1.9 0 ${z_pos - 0.98}">
        <Shape>
            <Box size="3.0 10.0 0.001" />
            <Appearance>
                <Material diffuseColor="1 1 1" />
            </Appearance>
        </Shape>
    </Transform>
    <Transform translation="-1.9 0 ${z_pos - 0.98}">
        <Shape>
            <Box size="3.0 10.0 0.001" />
            <Appearance>
                <Material diffuseColor="1 1 1" />
            </Appearance>
        </Shape>
    </Transform>

    <!-- HUD - Hide Controls -->
    <Transform translation="1.6 -1.6 ${z_pos - 4}">
        <TouchSensor DEF="TS_HideControls" />
        <Shape>
            <Box size="0.05 0.05 0.001" />
            <Appearance>
                <Material diffuseColor="0.8 0 0" />
                <ImageTexture url="&amp;quot;http://www.meme-hazard.org/x3d/textures/hideHUDButton.png&amp;quot;" />
            </Appearance>
        </Shape>
    </Transform>

    <!-- HUD Widgets -->
    <Switch DEF="Sw_HUDWidgets" whichChoice="1">
        <Group />
        <Group>

            <!-- HUD - Rotation Ball -->
            <Transform translation="1.325 -1.325 ${z_pos - 4}">
                <SphereSensor DEF="SS_RotationSphere" />
                <Transform DEF="T_RotationSphere">
                    <Shape>
                        <Sphere radius="0.25" />
                        <Appearance>
                            <Material diffuseColor="0 0.2 1" />
                        </Appearance>
                    </Shape>
                    <Shape>
                        <Cylinder height="0.01" radius="0.255" />
                        <Appearance>
                            <Material diffuseColor="1 0 0" />
                        </Appearance>
                    </Shape>
                    <Transform rotation="0 0 1 1.5708">
                        <Shape>
                            <Cylinder height="0.01" radius="0.255" />
                            <Appearance>
                                <Material diffuseColor="0 1 0" />
                            </Appearance>
                        </Shape>
                    </Transform>
                </Transform>
                <ROUTE fromField="rotation_changed" fromNode="SS_RotationSphere" toField="rotation" toNode="T_ViewpointRotation" />
                <ROUTE fromField="rotation_changed" fromNode="SS_RotationSphere" toField="rotation" toNode="T_RotationSphere" />
            </Transform>

            <!-- HUD - Current entity -->
            <Switch DEF="Sw_AnatomyLabel" whichChoice="0">
                <Group />
                <Group>
                    <Transform translation="-1.35 -1.4925 ${z_pos - 4}">
                        <Shape>
                            <Text DEF="Tx_AnatomyLabel" string="&amp;quot;&amp;quot;">
                                <FontStyle family="&amp;quot;SANS&quot; " size="0.14" />
                            </Text>
                        </Shape>
                    </Transform>
                    <Transform translation="-1.35 -1.58 ${z_pos - 4}">
                        <Shape>
                            <Text DEF="Tx_AnatomyGroupLabel" string="&amp;quot;test&amp;quot;">
                                <FontStyle family="&amp;quot;SANS&amp;quot;" size="0.07" />
                            </Text>
                        </Shape>
                    </Transform>
                </Group>
            </Switch>

            <!-- HUD - 'More Info' button -->
            <Switch DEF="Sw_AnatomyURL" whichChoice="0">
                <Group>
                    <Transform translation="-1.5 -1.5 ${z_pos - 4}" />
                </Group>
                <Group>
                    <Transform translation="-1.5 -1.5 ${z_pos - 4}56">
                        <Anchor DEF="A_AnatomyMoreInfo">
                            <Shape>
                                <Box size="0.2 0.2 0.001" />
                                <Appearance>
                                    <Material diffuseColor="0.3 0.3 0.3" />
                                    <ImageTexture url="&amp;quot;http://www.meme-hazard.org/x3d/textures/moreInfoButton.png&amp;quot;" />
                                </Appearance>
                            </Shape>
                        </Anchor>
                    </Transform>
                </Group>
            </Switch>

            <!-- HUD - Zoom control -->
            <Group>
                <PlaneSensor DEF="PS_Zoom" maxPosition="0 1" minPosition="0 0" />
                <Transform translation="1.525 0.65 ${z_pos - 4}">
                    <Shape>
                        <Box size="0.02 1.0 0.001" />
                        <Appearance>
                            <Material diffuseColor="0.7 0.7 0.7" />
                        </Appearance>
                    </Shape>
                    <Transform DEF="T_SliderZoom" translation="0 -0.3333 0.002">
                        <Shape>
                            <Box size="0.15 0.08 0.002" />
                            <Appearance>
                                <Material diffuseColor="0 0 0" />
                                <ImageTexture url="&amp;quot;http://www.meme-hazard.org/x3d/textures/zoomSlider.png&amp;quot;" />
                            </Appearance>
                        </Shape>
                    </Transform>
                </Transform>
            </Group>
        </Group>
    </Switch>

    <!-- Script - Toggle display of HUD widgets -->
    <Script DEF="Scr_HUDVisibility">
        <field accessType="inputOnly" name="clickEvent_Hide" type="SFTime"/>
        <field accessType="outputOnly" name="switch_hudWidgets" type="SFInt32"/>
&lt;![CDATA[
ecmascript:
var shown = true;
function clickEvent_Hide()
{
    if (shown)
    {
        switch_hudWidgets = 1;
        shown = false;
    }
    else
    {
        switch_hudWidgets = 0;
        shown = true;
    }
}
]]&gt;
    </Script>
    <ROUTE fromField="touchTime" fromNode="TS_HideControls" toField="clickEvent_Hide" toNode="Scr_HUDVisibility" />
    <ROUTE fromField="switch_hudWidgets" fromNode="Scr_HUDVisibility" toField="whichChoice" toNode="Sw_HUDWidgets" />

    <!-- Script - Implement zoom control bar -->
    <Script DEF="Scr_Zoom">
        <field accessType="inputOnly" name="planeMotionVector" type="SFVec3f"/>
        <field accessType="inputOnly" name="planeMotionActive" type="SFBool"/>
        <field accessType="outputOnly" name="scale" type="SFVec3f"/>
        <field accessType="outputOnly" name="bar_translation" type="SFVec3f"/>
&lt;![CDATA[
ecmascript:
var first = true;
function planeMotionActive (value)
{
    if (value == false)
        first = false;
}
function planeMotionVector (value)
{
	// Calculate input as distance along bar from actual slider level
	var y = value[1] - 0.15;

    // Clamp y to 0..1
    if (y &gt; 1) y = 1;
    if (y &lt; 0) y = 0;

    Browser.print("V1 = " + value[1] + "; y = " + y + "\n");

    // Calculate scale level
    s = Math.pow(4,y) - 0.3;
    scale[0] = s;
    scale[1] = s;
    scale[2] = s;

    Browser.print("s = " + s + "\n");

    // Set slider location
    if      (y &gt; 1.0)
        bar_translation[1] = 0.5;
    else if (y &lt; 0)
        bar_translation[1] = -0.5;
    else
        bar_translation[1] = y - 0.5;
}
]]&gt;
    </Script>
    <ROUTE fromField="trackPoint_changed" fromNode="PS_Zoom" toField="planeMotionVector" toNode="Scr_Zoom" />
    <ROUTE fromField="isActive" fromNode="PS_Zoom" toField="planeMotionActive" toNode="Scr_Zoom" />
    <ROUTE fromField="scale" fromNode="Scr_Zoom" toField="scale" toNode="T_Zoom" />
    <ROUTE fromField="bar_translation" fromNode="Scr_Zoom" toField="translation" toNode="T_SliderZoom" />

    <Script DEF="Scr_Selection">
        <field accessType="inputOnly" name="ce_Clear" type="SFTime"/>
        <field accessType="outputOnly" name="labelText" type="MFString"/>
        <field accessType="outputOnly" name="labelGroupText" type="MFString"/>
        <field accessType="outputOnly" name="labelSwitch" type="SFInt32"/>
        <field accessType="outputOnly" name="urlText" type="MFString"/>
        <field accessType="outputOnly" name="urlSwitch" type="SFInt32"/>
        <c:if test="${not empty scnod.sceneObjectDescriptors}">
            <c:forEach var="scnod" items="${scnod.sceneObjectDescriptors}">
                <field accessType="inputOnly" name="ce_${scnod.entity.id}" type="SFTime"/>
                <field accessType="outputOnly" name="em_${scnod.entity.id}" type="SFColor"/>
            </c:forEach>
        </c:if>
&lt;![CDATA[
ecmascript:
function getBlack()
{
    var color = new SFColor();
    color[0] = 0;
    color[1] = 0;
    color[2] = 0;
    return color;
}
function getHighlightColor()
{
    var color = new SFColor();
    color[0] = 0;
    color[1] = 0.7;
    color[2] = 0.6;
    return color;
}
function getHotSpotColor()
{
    var color = new SFColor();
    color[0] = 0.7;
    color[1] = 0.7;
    color[2] = 0;
    return color;
}
function setLabel(text, grp)
{
    labelText[0] = text;
    labelGroupText[0] = grp;
    labelSwitch = 1;
}
function setUrl(url)
{
    urlText[0] = url;
    urlSwitch = 1;
}
function clearAll()
{
    var blankColor = getBlack();

    <c:if test="${not empty scnd.sceneObjectDescriptors}"><c:forEach var="scnod" items="${scnd.sceneObjectDescriptors}">em_${scnod.entity.id} = blankColor;
    </c:forEach>
                </c:if>
}
function ce_Clear(val)
{
    clearAll();
    labelSwitch = 0;
    urlSwitch = 0;
}
<c:if test="${not empty scnd.sceneObjectDescriptors}"><c:forEach var="scnod" items="${scnd.sceneObjectDescriptors}">
function ce_${scnod.entity.id}(val)
{
    clearAll();
    em_${scnod.entity.id} = getHighlightColor();
    setLabel("${scnod.name}", "");
    urlSwitch = 0;
}
</c:forEach>
                </c:if>
]]&gt;
    </Script>
    <ROUTE fromField="touchTime" fromNode="TS_Background" toField="ce_Clear" toNode="Scr_Selection" />
    <ROUTE fromField="labelText" fromNode="Scr_Selection" toField="string" toNode="Tx_AnatomyLabel" />
    <ROUTE fromField="labelGroupText" fromNode="Scr_Selection" toField="string" toNode="Tx_AnatomyGroupLabel" />
    <ROUTE fromField="labelSwitch" fromNode="Scr_Selection" toField="whichChoice" toNode="Sw_AnatomyLabel" />
    <ROUTE fromField="urlText" fromNode="Scr_Selection" toField="url" toNode="A_AnatomyMoreInfo" />
    <ROUTE fromField="urlSwitch" fromNode="Scr_Selection" toField="whichChoice" toNode="Sw_AnatomyURL" />
    <c:if test="${not empty scnd.sceneObjectDescriptors}">
        <c:forEach var="scnod" items="${scnd.sceneObjectDescriptors}">
            <ROUTE fromField="touchTime" fromNode="TS_${scnod.entity.id}" toField="ce_${scnod.entity.id}" toNode="Scr_Selection" />
            <ROUTE fromField="em_${scnod.entity.id}" fromNode="Scr_Selection" toField="emissiveColor" toNode="Mtl_${scnod.entity.id}" />
        </c:forEach>
    </c:if>

</Scene>
</X3D>
</jsp:root>
