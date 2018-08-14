/**************************************************************************
    Class - AE Focus Panel

    Shows information concerning current exploration focus
    Provides UI controls to shift focus

**************************************************************************/
var AE_CommandPanel = function (container, controller, config)
{
    // Components
    this.container = container;
    this.explorer = controller.explorer;
    this.config = config;

    // Embellish object as UI panel
    UI_Panel.embellish(this, "AE_CommandPanel");

    // Build UI and show
    this.buildUI();
};




// Create the UI for the picking panel
AE_CommandPanel.prototype.buildUI = function ()
{
    // Clear panel
    this.clear();

    // Component - Title
    var div_header = DOMUtils.div("header");
    div_header.appendChild(DOMUtils.header(1, "Change Exploration"));

    // Component - Form
    div_form_entity = this.buildUI_exploreEntity();


    // Assemble - Complete
    this.container.appendChild(div_header);
    this.container.appendChild(div_form_entity);
};




AE_CommandPanel.prototype.consumeEvent = function (data, type)
{
    if (type == AE_Explorer.EVENT_ERROR)
    {
        alert("Error: " + data.message);
    }
};




AE_CommandPanel.prototype.buildUI_exploreEntity = function ()
{
    // Self reference
    var that = this;

    var div = DOMUtils.div("form");

    // Component - Entity Text box
    input_entity = DOMUtils.input("", "", "text");
    SG_TextCompletion.applyTextCompletion(input_entity);

    // Component - Focus button
    var button = DOMUtils.button("", "", "Explore", function () {
        that.explorer.exploreEntityByName(input_entity.value);
    });

    // Link - field 'enter' key up triggers button
    $(input_entity).keyup(function(event) { if(event.keyCode == 13) $(button).click(); });

    // Assemble
    div.appendChild(DOMUtils.label("Entity:"));
    div.appendChild(input_entity);
    div.appendChild(button);

    return div;
};
