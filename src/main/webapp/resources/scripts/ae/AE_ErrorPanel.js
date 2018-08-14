var AE_ErrorPanel = function (container)
{
    // Store references
    this.container = container;

    // Embellish
    UI_Panel.embellish(this, "AE_ErrorPanel");

    // Set default states
    this.state = false;
    this.VISIBILITY_SPEED = 100;
};




AE_ErrorPanel.prototype.consumeEvent = function (data, type)
{
    if (type == AE_Explorer.EVENT_ERROR)
    {
        // Clear
        this.clear();

        // Add content
        var div_content = DOMUtils.div("text");
        div_content.appendChild(DOMUtils.header(3, "Error"));
        div_content.appendChild(DOMUtils.text(data.message));
        this.container.appendChild(div_content);

        // Toggle visibility
        $(this.container).show().delay(3000).fadeOut();
    }
};
