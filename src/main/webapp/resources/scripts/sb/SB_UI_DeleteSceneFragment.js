/**************************************************************************
    Class - UI - Delete Scene Fragment

**************************************************************************/
var SB_UI_DeleteSceneFragment = function (container, builder)
{
    this.container = container;
    this.builder = builder;

    // Embellish object as UI panel
    UI_Panel.embellish(this, "SB_UI_DeleteSceneFragment");

    this.htmlElements = {};
};



SB_UI_DeleteSceneFragment.prototype.buildUI = function ()
{
    // Self-reference
    var that = this;

    // Clear any old content
    this.clear();

    // Element - Text
    var text = DOMUtils.p(DOMUtils.text("Are you sure you wish to delete the Scene Fragment named \"" + this.fragment.name + "\"?"));

    // Elements - Buttons
    var button_delete = DOMUtils.button("", "", "Delete", function () { that._evt_delete(); });
    var button_cancel = DOMUtils.button("", "", "Cancel", function () { that._evt_closeForm(); });

    // Assemble - Buttons
    var glue_buttons = DOMUtils.div("glue_center", "", "");
    glue_buttons.appendChild(button_delete);
    glue_buttons.appendChild(button_cancel);

    // Assemble
    this.container.appendChild(DOMUtils.header(1, "Confirm Deletion"));
    this.container.appendChild(text);
    this.container.appendChild(glue_buttons);
};




SB_UI_DeleteSceneFragment.prototype.open = function (fragment)
{
    if (this.isVisible())
        console.log("Popup form already open. Ignoring request to Delete Scene Fragment");
    else
    {
        // Store fragment for use
        this.fragment = fragment;

        this.buildUI();
        this.show();
    }
};




SB_UI_DeleteSceneFragment.prototype._evt_closeForm = function ()
{
    // Self reference
    var that = this;

    // Close and clear form
    this.hide(function () { that.clear(); });
};




SB_UI_DeleteSceneFragment.prototype._evt_delete = function ()
{
    // Self reference
    var that = this;

    // Pass to builder for execution
    this.builder.deleteSceneFragment(this.fragment.id);

    // Close and clear form
    this.hide(function () { that.clear(); });
};
