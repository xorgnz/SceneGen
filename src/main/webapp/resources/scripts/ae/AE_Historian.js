/**************************************************************************
    Class - AE Historian

    Keeps track of previous Explorer view events
    Tracks:
    - Changes in explored object
    - Changes in focused object

**************************************************************************/
var AE_Historian = function ()
{
    this.events = [];
    this.cursor = null;

    // Embellish as event dispatcher
    TEventDispatcher.embellish(this, "AE_Lens");
};

// Enumeration of History event types
AE_Historian.EVENT_EXPLORE = 1;




// Record an exploration event in the history
AE_Historian.prototype.addExploreEvent = function (entityId, entityLabel)
{
    // Break if we're adding a duplicate event to the top of the history
    var last_event = this.events[this.events.length - 1];
    if (last_event && last_event.type == AE_Historian.EVENT_EXPLORE && last_event.entityId == entityId)
        return;

    // Break if we're adding a copy of the event under the cursor
    var cursor_event = this.events[this.cursor];
    if (cursor_event && cursor_event.type == AE_Historian.EVENT_EXPLORE && cursor_event.entityId == entityId)
        return;

    // Save event to history
    this.events.push({
        type: AE_Historian.EVENT_EXPLORE,
        entityId: entityId,
        entityLabel: entityLabel,
        position: this.events.length,
    });

    // Reset cursor
    this.resetCursor();

    // Notify listeners of change
    this.fireEvent();
};



// Clear the history
AE_Historian.prototype.clearHistory = function ()
{
    this.events = [];
};




// Retrieve the current cursor position
AE_Historian.prototype.getCursorPosition = function ()
{
    return this.cursor;
};




// Get the current history pointer event
AE_Historian.prototype.getEventAtCursor = function ()
{
    if (this.cursor >= 0 && this.cursor < this.events.length)
        return this.events[this.cursor];
    else
        return null;
};




// Get the history
AE_Historian.prototype.getHistory = function ()
{
    return this.events;
};




// Shift the history pointer forward and return the new event
AE_Historian.prototype.moveCursorForward = function ()
{
    // If there are no events, return null
    if (this.events.length == 0)
        return null;

    // Shift event pointer forward
    if (this.cursor < this.events.length - 1)
        this.cursor ++;

    // Notify listeners of change
    this.fireEvent();
};




// Shift the history pointer backward and return the new event
AE_Historian.prototype.moveCursorBack = function ()
{
    // If there are no events, return 0
    if (this.events.length == 0)
        return null;

    // Shift event pointer back
    if (this.cursor > 0)
        this.cursor --;

    // Notify listeners of change
    this.fireEvent();
};




// Reset the history cursor position to point at the most recent event
AE_Historian.prototype.resetCursor = function ()
{
    this.cursor = this.events.length - 1;

    // Notify listeners of change
    this.fireEvent();
};




// Set the current cursor position
AE_Historian.prototype.setCursorPosition = function (position)
{
    this.cursor = position;

    // Notify listeners of change
    this.fireEvent();
};



