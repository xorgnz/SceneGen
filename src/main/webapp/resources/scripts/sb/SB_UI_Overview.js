    /**************************************************************************
    Class - SB Info Panel

    Shows information about scene under construction. Includes overview of
    scene and information about individual Scene Fragments

**************************************************************************/
var SB_UI_Overview = function (container, builder, scene, controller, config)
{
    this.container = container;
    this.builder = builder;
    this.config = config;
    this.controller = controller;
    this.scene = scene;

    // UI element storage
    this.uiElements = {};

    // Embellish object as UI panel, Event Dispatcher
    UI_Panel.embellish(this, "SB_UI_Overview");
    TEventDispatcher.embellish(this, "SB_UI_Overview");

    this.buildUI();
};
SB_UI_Overview.EVT_SELECTION = 1;
SB_UI_Overview.EVT_VISIBILITY_TOGGLE = 2;




// Create the UI for the picking panel
SB_UI_Overview.prototype.buildUI = function ()
{
    // Clear
    this.clear();
    this.buildUI_scene();
    this.buildUI_fragments();
};




SB_UI_Overview.prototype.buildUI_scene = function ()
{
    var that = this;

    // Element - Edit button
    var button_edit = DOMUtils.button("", "sb_edit_button", "Edit", function () {
        that.controller.uiElements.editScenePopup.open();
    });

    // Element - Add Query Fragment button
    var button_addQueryFragment = DOMUtils.button("lower_button", "sb_add_query_fragment_button", "Add Query Fragment", function () {
        that.controller.uiElements.addQueryFragmentPopup.open();
    });

    // Element - Add List Fragment button
    var button_addListFragment = DOMUtils.button("lower_button", "sb_add_list_fragment_button", "Add List Fragment", function () {
        that.controller.uiElements.addListFragmentPopup.open();
    });

    // Element - Save Viewpoint button
    var button_saveViewpoint = DOMUtils.button("lower_button", "sb_save_viewpoint", "Save Viewpoint", function () {
        that._evt_saveViewpoint();
    });

    // Assemble
    var div_controls = DOMUtils.div("controls");
    div_controls.appendChild(button_edit);
    div_controls.appendChild(button_addQueryFragment);
    div_controls.appendChild(button_addListFragment);
    div_controls.appendChild(button_saveViewpoint);
    var div_scene = DOMUtils.div("scene");
    div_scene.appendChild(div_controls);
    div_scene.appendChild(DOMUtils.header(1, "Scene: " + this.scene.getName()));
    this.container.appendChild(div_scene);
};




SB_UI_Overview.prototype.buildUI_fragments = function ()
{
    // Map for member lines
    this.uiElements.memberLineMap = {};

    // Element - Fragments container
    var div_fragments = DOMUtils.div("fragments");

    var fragments = this.scene.getFragments();
    for (var i = 0 ; i < fragments.length ; i ++)
    {
        if (fragments[i].type == 0)
            div_fragments.appendChild(this.buildUI_queryFragment(fragments[i]));
        else if (fragments[i].type == 1)
            div_fragments.appendChild(this.buildUI_listFragment(fragments[i]));
        else
            div_fragments.appendChild(this.buildUI_thirdpartyFragment(fragments[i]));
    }

    // Assemble
    this.container.appendChild(div_fragments);
};




SB_UI_Overview.prototype.buildUI_listFragment = function (fragment)
{
    // Self reference
    var that = this;

    var text_desc = "";

    // Element - Delete button
    var button_delete = DOMUtils.button("sb_delete_button", "", "Delete", function () {
        that.controller.uiElements.deleteSceneFragmentPopup.open(this.fragment);
    });
    button_delete.fragment = fragment;

    // Element - Edit button
    var button_edit = DOMUtils.button("sb_edit_button", "", "Edit", function () {
        that.controller.uiElements.editListFragmentPopup.open(this.fragment);
    });
    button_edit.fragment = fragment;

    // Element - Header
    var header = DOMUtils.header(1, "Fragment: " + fragment.name + " (" + fragment.id + ")");

    // Element - Description - Query Info
    text_desc += "Scene Type: <b>List</b><br/>";

    // Element - Description - Asset Set
    text_desc += "Assets from ";
    if (fragment.assetSet.id >= 0)
        text_desc += "<b>" + this.config.getAssetSet(fragment.assetSet.id).name + " (" + fragment.assetSet.id + ")</b><br/>";
    else
        text_desc += "<b>Default asset set</b><br/>";

    // Element - Description - stylesheet
    text_desc += "Styles from ";
    if (fragment.stylesheet.id >= 0)
        text_desc += "<b>" + this.config.getStylesheet(fragment.stylesheet.id).name + " (" + fragment.stylesheet.id + ")</b><br/>";
    else
        text_desc += "<b>Default stylesheet</b><br/>";

    // Element - Description
    var div_text = DOMUtils.div("text", "", text_desc);

    // Scene Object list
    var fs_object_box = DOMUtils.fieldset("object_box", "", "", "Hide entities");
    var div_object_list = DOMUtils.div();
    var div_hidden_message = DOMUtils.div("hidden_message","", "Entities hidden");
    $(div_hidden_message).hide();
    fs_object_box.legend.onclick = function ()
    {
        if ($(div_object_list).is(":visible"))
        {
            this.innerHTML = "Show entities";
            $(div_object_list).hide();
            $(div_hidden_message).show();
        }
        else
        {
            this.innerHTML = "Hide entities";
            $(div_object_list).show();
            $(div_hidden_message).hide();
        }
    };
    if (fragment.members.length == 0)
        div_object_list.appendChild(DOMUtils.span("problem", "", "No scene objects contributed"));
    else
    {
        // List objects included
        for (var j = 0 ; j < fragment.members.length; j++)
        {
            var line = new SB_UI_Overview_MemberLine(fragment.members[j], j % 2, this);
            this.uiElements.memberLineMap[fragment.members[j].id] = line;
            div_object_list.appendChild(line.container);
        }
    }
    fs_object_box.appendChild(div_hidden_message);
    fs_object_box.appendChild(div_object_list);

    // Assemble
    var div_controls = DOMUtils.div("controls");
    div_controls.appendChild(button_edit);
    div_controls.appendChild(button_delete);
    var div_fragment = DOMUtils.div("fragment");
    div_fragment.appendChild(div_controls);
    div_fragment.appendChild(header);
    div_fragment.appendChild(div_text);
    div_fragment.appendChild(fs_object_box);

    return div_fragment;
};




SB_UI_Overview.prototype.buildUI_queryFragment = function (fragment)
{
    // Self reference
    var that = this;

    var text_desc = "";

    // Element - Delete button
    var button_delete = DOMUtils.button("sb_delete_button", "", "Delete", function () {
        that.controller.uiElements.deleteSceneFragmentPopup.open(this.fragment);
    });
    button_delete.fragment = fragment;

    // Element - Edit button
    var button_edit = DOMUtils.button("sb_edit_button", "", "Edit", function () {
        that.controller.uiElements.editQueryFragmentPopup.open(this.fragment);
    });
    button_edit.fragment = fragment;

    // Element - Header
    var header = DOMUtils.header(1, "Fragment: " + fragment.name + " (" + fragment.id + ")");

    // Element - Description - Query Info
    text_desc += "Scene Type: <b>Query</b><br/>";
    text_desc += "&nbsp;&nbsp;&nbsp;&nbsp; Name: <b>" +
                 this.config.getQuery(fragment.query.id).name +
                 " (" + fragment.query.id + ")</b><br/>";
    for (var j = 0 ; j < fragment.queryParamValues.length ; j ++)
        text_desc += "&nbsp;&nbsp;&nbsp;&nbsp; Parameters: <b>" +
                     fragment.queryParamValues[j].label + " = " + fragment.queryParamValues[j].value + "</b><br/><br/>";

    // Element - Description - Asset Source
    text_desc += "Assets from ";
    if (fragment.assetSet.id >= 0)
        text_desc += "<b>" + this.config.getAssetSet(fragment.assetSet.id).name + " (" + fragment.assetSet.id + ")</b><br/>";
    else
        text_desc += "<b>Default asset set</b><br/>";

    // Element - Description - stylesheet
    text_desc += "Styles from ";
    if (fragment.stylesheet.id >= 0)
        text_desc += "<b>" + this.config.getStylesheet(fragment.stylesheet.id).name + " (" + fragment.stylesheet.id + ")</b><br/>";
    else
        text_desc += "<b>Default stylesheet</b><br/>";

    // Element - Description
    var div_text = DOMUtils.div("text", "", text_desc);

    // Sort members into present and missing lists
    var members_present = [];
    var members_missing = [];
    for (var i = 0 ; i < fragment.members.length; i++)
        if (!fragment.members[i].missing)
            members_present.push(fragment.members[i]);
        else
            members_missing.push(fragment.members[i]);

    // Present Scene Object list
    // Scene Object list
    var fs_object_box = DOMUtils.fieldset("object_box", "", "", "Hide entities");
    var div_object_list = DOMUtils.div();
    var div_hidden_message = DOMUtils.div("hidden_message","", "Entities hidden");
    $(div_hidden_message).hide();
    fs_object_box.legend.onclick = function ()
    {
        if ($(div_object_list).is(":visible"))
        {
            this.innerHTML = "Show entities";
            $(div_object_list).hide();
            $(div_hidden_message).show();
        }
        else
        {
            this.innerHTML = "Hide entities";
            $(div_object_list).show();
            $(div_hidden_message).hide();
        }
    };
    if (fragment.members.length == 0)
        div_object_list.appendChild(DOMUtils.span("problem", "", "No scene objects contributed"));
    else
    {
        // List objects included
        for (var j = 0 ; j < members_present.length; j++)
        {
            var line = new SB_UI_Overview_MemberLine(members_present[j], j % 2, this);
            this.uiElements.memberLineMap[members_present[j].id] = line;
            div_object_list.appendChild(line.container);
        }
    }
    fs_object_box.appendChild(div_hidden_message);
    fs_object_box.appendChild(div_object_list);

    // Missing Scene Object list
    var div_objects_missing_toggle = null;
    var div_objects_missing = null;
    if (members_missing.length > 0)
    {
        div_objects_missing_toggle = DOMUtils.button("toggle_button", "", "Some entities in this fragment do not have models. <br/> Click here to show / hide a list of these entities", function ()
        {
            $(div_objects_missing).toggle();
        });
        div_objects_missing = DOMUtils.div("object_list", "", "", "display:none");

        // List objects included
        for (var i = 0 ; i < members_missing.length; i++)
        {
            var line = new SB_UI_Overview_MemberLine(members_missing[i], i % 2, this);
            this.uiElements.memberLineMap[members_missing[i].id] = line;
            div_objects_missing.appendChild(line.container);
        }
    }

    // Assemble
    var div_controls = DOMUtils.div("controls");
    div_controls.appendChild(button_edit);
    div_controls.appendChild(button_delete);
    var div_fragment = DOMUtils.div("fragment");
    div_fragment.appendChild(div_controls);
    div_fragment.appendChild(header);
    div_fragment.appendChild(div_text);
    div_fragment.appendChild(fs_object_box);
    if (div_objects_missing_toggle) div_fragment.appendChild(div_objects_missing_toggle);
    if (div_objects_missing)        div_fragment.appendChild(div_objects_missing);

    return div_fragment;
};




SB_UI_Overview.prototype.buildUI_thirdpartyFragment = function (fragment)
{
    // Element - Header
    var header = DOMUtils.header(1, "Fragment: " + fragment.name + " (" + fragment.id + ")");

    // Element - Description
    var div_text = DOMUtils.div("text", "", "This fragment was created in another application and cannot be edited in the scene builder.");

    // Scene Object list
    var fs_object_box = DOMUtils.fieldset("object_box", "", "", "Hide entities");
    var div_object_list = DOMUtils.div();
    var div_hidden_message = DOMUtils.div("hidden_message","", "Entities hidden");
    $(div_hidden_message).hide();
    fs_object_box.legend.onclick = function ()
    {
        if ($(div_object_list).is(":visible"))
        {
            this.innerHTML = "Show entities";
            $(div_object_list).hide();
            $(div_hidden_message).show();
        }
        else
        {
            this.innerHTML = "Hide entities";
            $(div_object_list).show();
            $(div_hidden_message).hide();
        }
    };
    if (fragment.members.length == 0)
        div_object_list.appendChild(DOMUtils.span("problem", "", "No scene objects contributed"));
    else
    {
        // List objects included
        for (var j = 0 ; j < fragment.members.length; j++)
        {
            var line = new SB_UI_Overview_MemberLine(fragment.members[j], j % 2, this);
            this.uiElements.memberLineMap[fragment.members[j].id] = line;
            div_object_list.appendChild(line.container);
        }
    }
    fs_object_box.appendChild(div_hidden_message);
    fs_object_box.appendChild(div_object_list);

    // Assemble
    var div_fragment = DOMUtils.div("fragment");
    div_fragment.appendChild(header);
    div_fragment.appendChild(div_text);
    div_fragment.appendChild(fs_object_box);

    return div_fragment;
};




// Listener for SB_Lens selection change.
// - object : CGA_GeometryObject
SB_UI_Overview.prototype.selectChange = function (data)
{
    // Grab map
    var map = this.uiElements.memberLineMap;
    var object = data.object;

    // Deselect old line
    for (var key in map)
    {
        if (object && key == object.id)
            map[key].setSelected(true);
        else
            map[key].setSelected(false);
    }
};




SB_UI_Overview.prototype._evt_saveViewpoint = function ()
{
    this.builder.updateScene_viewpoint();
};




var SB_UI_Overview_MemberLine = function (member, even, parent)
{
    // Create element storage
    this.htmlElements = {};

    // Create container
    this.container = DOMUtils.div("sb_overview_member_line");
    if (even)
        this.container.className += " even";
    else
        this.container.className += " odd";
    this.initialContainerClass = this.container.className;

    // Create components
    this.htmlElements.label = DOMUtils.div("label", "", member.entity.name);
    if (! member.missing && member.visible)
    {
        this.htmlElements.label = DOMUtils.div("label in_scene", "", member.entity.name);
        this.htmlElements.button_vis = DOMUtils.div("button hide", "", "Hide");
    }
    else if (! member.missing && ! member.visible)
    {
        this.htmlElements.label = DOMUtils.div("label not_in_scene", "", member.entity.name);
        this.htmlElements.button_vis = DOMUtils.div("button show", "", "Show");
    }
    else if (member.missing)
        this.htmlElements.label = DOMUtils.div("label missing", "", member.entity.name + " (no model data)");

    // Action - Select object when label is clicked
    this.htmlElements.label.onclick = function () {
        parent.fireEvent(member.id, SB_UI_Overview.EVT_SELECTION);
    };

    // Action - Toggle visibility of object when button is clicked
    if (this.htmlElements.button_vis)
        this.htmlElements.button_vis.onclick = function () {
            parent.fireEvent(
                {
                    id: member.id,
                    visible: ! member.visible
                }, SB_UI_Overview.EVT_VISIBILITY_TOGGLE);
        };

    // Assemble
    this.container.appendChild(this.htmlElements.label);
    if (this.htmlElements.button_vis) this.container.appendChild(this.htmlElements.button_vis);
    this.container.appendChild(DOMUtils.div("", "", "", "clear: both;"));
};




SB_UI_Overview_MemberLine.prototype.setSelected = function(selected)
{
    if (selected)
        this.container.className += " selected";
    else
        this.container.className = this.initialContainerClass;
};





