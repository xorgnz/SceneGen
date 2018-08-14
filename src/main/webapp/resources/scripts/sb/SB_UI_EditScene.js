/**************************************************************************
    Class - SB Info Panel

    Shows information about scene under construction. Includes overview of
    scene and information about individual Scene Fragments

**************************************************************************/
var SB_UI_EditScene = function (container, scene, builder)
{
    this.container = container;
    this.scene = scene;
    this.builder = builder;

    // Embellish object as UI panel
    UI_Panel.embellish(this, "SB_UI_EditScene");

    this.htmlElements = {};
};




SB_UI_EditScene.prototype.buildUI = function ()
{
    // Self reference
    var that = this;

    // Clear
    this.clear();

    // Element - Name field
    this.htmlElements.input_name = DOMUtils.input("", this.scene.getName(), "text");

    // Element - Buttons
    var button_save = DOMUtils.button("", "", "Save Changes", function () { that._evt_saveChanges(); });
    var button_cancel = DOMUtils.button("", "", "Cancel", function () { that._evt_closeForm(); });

    // Assemble - Form
    var glue_form = DOMUtils.div("glue", "", "");
    glue_form.appendChild(DOMUtils.label("Name"));
    glue_form.appendChild(this.htmlElements.input_name);
    glue_form.appendChild(document.createElement("br"));

    // Assemble - Buttons
    var glue_buttons = DOMUtils.div("glue_center", "", "");
    glue_buttons.appendChild(button_save);
    glue_buttons.appendChild(button_cancel);

    // Assemble
    this.container.appendChild(DOMUtils.header(1, "Edit Scene"));
    this.container.appendChild(glue_form);
    this.container.appendChild(glue_buttons);
};




SB_UI_EditScene.prototype.open = function ()
{
    if (this.isVisible())
        console.log("Popup form already open. Ignoring request to Edit Scene");
    else
    {
        this.buildUI();
        this.show();
    }
};




SB_UI_EditScene.prototype._evt_closeForm = function ()
{
    // Self reference
    var that = this;

    // Close and clear form
    this.hide(function () { that.clear(); });
};




SB_UI_EditScene.prototype._evt_saveChanges = function ()
{
    // Self reference
    var that = this;

    // Pass to builder for execution
    this.builder.updateScene(this.htmlElements.input_name.value);

    // Close and clear form
    this.hide(function () { that.clear(); });
};
