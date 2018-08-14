var TEventDispatcher = function () {};



// Attach TEventDispatcher functionality to a given object
TEventDispatcher.embellish = function (obj, type)
{
    // Validate arguments
    if (! type)
        throw "Cannot embellish object without type";

    // Log embellishment
    console.log("Embellishing " + type + " as Event Dispatcher");

    // Perform embellishment
    obj.fireEvent = this.fireEvent;
    obj.addListener = this.addListener;
    obj.removeListener = this.removeListener;

    // Initialize state variables
    obj.listenersByType = [];
    obj.universalListeners = [];
};




TEventDispatcher.fireEvent = function (data, type)
{
    if (! data)
        data = {};

    // Fire event to all listeners registered with given type, if set
    if (type)
        if (this.listenersByType[type])
            for (var i = 0 ; i < this.listenersByType[type].length ; i++)
                this.listenersByType[type][i](data, type);

    // Fire event to all generic listeners
    for (var i = 0 ; i < this.universalListeners.length ; i++)
        this.universalListeners[i](data, type);
};




// Add listener
// Listeners add without a type receive all events
TEventDispatcher.addListener = function (fn, type)
{
    // Check listener is valid
    if (typeof(fn) !== 'function')
        throw "Cannot register null listener";

    // Add type specific listener
    if (type)
    {
        // Create listener array for this type, if new
        if (! this.listenersByType[type])
            this.listenersByType[type] = [];

        // Add listener to array for type, if given
        this.listenersByType[type].push(fn);
    }
    else
        this.universalListeners.push(fn);
};




TEventDispatcher.removeListener = function (fn, type)
{
    // Use default type if none given
    if (! type)
        type = 0;

    // Remove listener
    if (this.listeners[type])
    {
        var index = this.listeners[type].indexOf(fn);

        if (index > -1)
            this.listeners[type].splice(index, 1);
    }
};


