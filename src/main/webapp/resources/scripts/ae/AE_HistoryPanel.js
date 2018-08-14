/**************************************************************************
    Class - AE History Panel

    Shows a history of exploration and focus events
    Allows users to return to previous focus and exploration states

**************************************************************************/
var AE_HistoryPanel = function (container, controller)
{
    // Save container
    this.container = container;
    this.controller = controller;
    this.lens = controller.lens;
    this.explorer = controller.explorer;
    this.historian = controller.historian;

    // Embellish object as UI panel
    UI_Panel.embellish(this, "AE_HistoryPanel");

    // Create UI Elements
    this.htmlElements =
    {
    };

    // Build UI and show
    this.buildUI();
    this.hide();
};




// Create the UI for the picking panel
AE_HistoryPanel.prototype.buildUI = function ()
{
    // Self reference
    var that = this;

    // Clear container
	this.container.innerHTML = "";

	// Add header
	this.container.appendChild(DOMUtils.header(1, "History"));

	// Add a row for each historic event
	var history = this.historian.getHistory();
	var historyCursor = this.historian.getCursorPosition();
	for (var i = history.length - 1 ; i >= 0 ; i--)
	{
	    var event = history[i];

	    // Create row
	    var div_row = DOMUtils.div("","","");

	    if (i % 2 == 0)
	        div_row.className = "row_even";
	    else
	        div_row.className = "row_odd";

	    // Mark cursor if appropriate
	    if (historyCursor == i)
	        div_row.className += " cursor";

	    if (event.type == AE_Historian.EVENT_EXPLORE)
	    {
	        // Create link to re-explore
	        var a = DOMUtils.link(
                "javascript:;",
                event.entityLabel,
                null,
                function ()
                {
                    that.historian.setCursorPosition(this.event.position);
                    that.explorer.exploreEntity(this.event.entityId);
                }
            );
            a.event = event;

            // Assemble row
            div_row.appendChild(DOMUtils.text("Explore: ", false, false));
            div_row.appendChild(a);
	    }
	    else if (event.type == AE_Historian.EVENT_FOCUS)
	    {
            // Create link to re-focus
	        var a = DOMUtils.link(
	            "javascript:;",
	            event.relationLabel + " - " + event.entityLabel,
	            null,
	            function ()
	            {
	                alert("fish");
	                that.explorer.exploreEntityRelationPair(
                        this.event.entityId,
                        this.event.relation,
                        this.event.relationLabel);
	            }
            );
            a.event = event;

            // Assemble row
            div_row.appendChild(DOMUtils.text("Focus: ", false, false));
            div_row.appendChild(a);
        }

	    // Assemble
	    this.container.appendChild(div_row);
	}
};
