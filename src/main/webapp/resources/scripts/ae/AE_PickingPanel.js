/**************************************************************************
    Class - AVA Picking Panel

    Shows name and links for currently hovered and selected objects

**************************************************************************/
var AE_PickingPanel = function (container, lens, entityRegistry)
{
    // Save container
    this.container = container;
    this.lens = lens;
    this.entityRegistry = entityRegistry;

    // Embellish object as UI panel
    UI_Panel.embellish(this, "AE_PickingPanel");

    // Build UI and show
    this.buildUI();
};




// Create the UI for the picking panel
AE_PickingPanel.prototype.buildUI = function ()
{
    // Clear container
	this.clear();

	// Components - Links
    this.aHovered = document.createElement("a");
    var hovered = this.entityRegistry.getEntityById(this.lens.getHoveredEntityId());
    this.aHovered.innerHTML = hovered ? hovered.name : "Nothing";
    this.aHovered.href = hovered ? hovered.id : "null";

    this.aSelected = document.createElement("a");
    var selected = this.entityRegistry.getEntityById(this.lens.getSelectedEntityId());
    this.aSelected.innerHTML = selected ? selected.name : "Nothing";
    this.aSelected.href = selected ? selected.id : "null";

    // Assemble
    this.container.appendChild(document.createTextNode("Pointing at: "));
    this.container.appendChild(this.aHovered);
    this.container.appendChild(document.createElement("br"));
    this.container.appendChild(document.createTextNode("Selected: "));
    this.container.appendChild(this.aSelected);
};


