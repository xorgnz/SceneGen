/**************************************************************************
    Class - AE Settings Panel

    Shows controls for configuration of the Anatomy Explorer

**************************************************************************/
var AE_SettingsPanel = function (container, controller)
{
    // Save container
    this.container = container;
    this.controller = controller;

    // Embellish object as UI panel
    UI_Panel.embellish(this, "AE_SettingsPanel");

    // Create UI Elements
    this.htmlElements =
    {
    };

    // Build UI and show
    this.buildUI();
    this.hide();
};




// Create the UI for the picking panel
AE_SettingsPanel.prototype.buildUI = function ()
{
    // Set up panel
    this.container.innerHTML = "";
	this.container.appendChild(DOMUtils.header(1, "Settings"));
};

