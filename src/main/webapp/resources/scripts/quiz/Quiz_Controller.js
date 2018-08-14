var Quiz_Controller = function(quizApp)
{
    this.quizApp = quizApp;
    this.lens = new Quiz_Lens();
};




// Handle keyboard down events
Quiz_Controller.prototype.handleKeyDown = function (e, state)
{
    // V - Toggle visibility of selected object
    if (e.keyCode == CGA_KeyCodes.KEY_V)
    {
        if (this.selectedObj)
        {
            this.selectedObj.setVisibility(false);
        }
    }

    // A - Make all objects visible
    if (e.keyCode == CGA_KeyCodes.KEY_A)
    {
        var objects = this.quizApp.cgaApp.gfxEngine.scene.getObjects();
        for (var i in objects)
            objects[i].setVisibility(true);
    }
};




// Mouse has been clicked while hovering over an object
Quiz_Controller.prototype.handleObjectClicked = function(obj)
{
    this.lens.setSelectedObject(obj);
};




// Mouse has stopped hovering over an object
Quiz_Controller.prototype.handleObjectHovered = function(obj)
{
    this.lens.setHoveredObject(obj);
};




// Help Item configuration
Quiz_Controller.prototype.helpConfig =
{
    name: "Quiz Controls",
    groups: [{
        items:
        [{
            control: "V",
            description: "Not sure yet",
        }],
    }],
};




// Interface - LoadListener
// Called when graphics engine has finished loading a scene
Quiz_Controller.prototype.loadFinished = function ()
{
    this.quizApp.queueStateChange(Quiz_Main.STATE_INTRO);
};


