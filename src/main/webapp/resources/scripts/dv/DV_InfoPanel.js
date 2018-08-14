/**************************************************************************
    Class - AVA Picking Panel

    Shows name and links for currently hovered and selected objects

**************************************************************************/
var DV_InfoPanel = function (container, lens, scene, visualizer)
{
    // Save container
    this.container = container;
    this.lens = lens;               // To determine which object is selected
    this.scene = scene;             // To retrieve scene objects
    this.visualizer = visualizer;   // Provides data on objects

    // Embellish object as UI panel
    UI_Panel.embellish(this, "DV_InfoPanel");

    // Build UI and show
    this.buildUI();
    this.show();
};




// Create the UI for the picking panel
DV_InfoPanel.prototype.buildUI = function ()
{
    // Clear
    this.clear();

    // Retrieve selected object
    var id = this.lens.getSelectedEntityId();
    var sceneObject = this.scene.getObjectById(id);

    if (sceneObject)
    {
        // Component - Header
        this.container.appendChild(DOMUtils.header(1, sceneObject.data.entity.name));

        // Retrieve data
        var data = this.visualizer.getDataByEntityId(sceneObject.data.entity.id);

        // Component - Data table
        if (data)
        {
            var table = DOMUtils.table(["Field", "Value"], "", ["field", "value"]);
            var i = 0;
            for (var key in data)
            {
                // Create row
                var tr = DOMUtils.table_row([key, data[key]]);
                table.appendChild(tr);

                // Style row
                $(tr).addClass(i % 2 ? "odd" : "even");
                i ++;
            }
            this.container.appendChild(table);
        }
        else
            this.container.appendChild(DOMUtils.text("No data for this structure"));
    }
    else
        this.container.appendChild(DOMUtils.header(1, "Nothing selected"));
};


