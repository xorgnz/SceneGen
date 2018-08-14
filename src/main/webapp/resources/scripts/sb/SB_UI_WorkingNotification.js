var SB_UI_WorkingNotification = function (container)
{
    // Store references
    this.container = container;


    // Embellish
    UI_Panel.embellish(this, "SB_UI_WorkingNotification");

    // Set default states
    this.state = false;
    this.VISIBILITY_SPEED = 100;

    // Build UI
    this.buildUI();
};




SB_UI_WorkingNotification.prototype.buildUI = function ()
{
    if (this.state)
    {
        // Clear
        this.clear();

        // Add content
        var div_content = DOMUtils.div("text");
        div_content.appendChild(DOMUtils.header(3, "Please wait.."));
        div_content.appendChild(DOMUtils.text(this.state));
        this.container.appendChild(div_content);

        // Toggle visibility
        this.show();
    }
    else
        this.hide();
};




SB_UI_WorkingNotification.prototype.update = function (data)
{
    this.state = data.workState;
    this.buildUI();
};
