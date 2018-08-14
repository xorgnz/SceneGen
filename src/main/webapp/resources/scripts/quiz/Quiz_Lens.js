var Quiz_Lens = function ()
{
    // Create state variables
    this.hoveredObj = null;
    this.highlightedObj = null;
    this.selectedObj = null;
};
// Constants
// Colors for hover and selection highlight
Quiz_Lens.HOVERED_MTL  = { ambient: "#000000", diffuse: "#000000", specular: "#000000", emissive: "#FFFF00", shininess: 200 };
Quiz_Lens.HIGHLIGHTED_MTL  = { ambient: "#00ffff", diffuse: "#00ffff", specular: "#000000", emissive: "#00ffff", shininess: 200 };
Quiz_Lens.SELECTED_MTL = { ambient: "#000000", diffuse: "#000000", specular: "#000000", emissive: "#ff00ff", shininess: 200 };



Quiz_Lens.prototype.clear = function ()
{
    // Deselect currently highlighted object
    if (this.highlightedObj)
    {
        this.highlightedObj.resetMaterial();
        this.highlightedObj = null;
    }

    // Deselect currently selected object
    if (this.selectedObj)
    {
        this.selectedObj.resetMaterial();
        this.selectedObj = null;
    }
};



Quiz_Lens.prototype.setHighlightedObject = function (obj)
{
    // Clear old highlighted object if a change is being made
    if (this.highlightedObj && this.highlightedObj != obj)
    {
        if (this.highlightedObj == this.selectedObj)
            this.highlightedObj.setMaterial(Quiz_Lens.SELECTED_MTL);
        else if (this.highlightedObj == this.hoveredObj)
            this.highlightedObj.setMaterial(Quiz_Lens.HOVERED_MTL);
        else
            this.hoveredObj.resetMaterial();
    }

    // Set highlighted object
    this.highlightedObj = obj;

    // Apply highlight material
    if (this.highlightedObj)
        this.highlightedObj.setMaterial(Quiz_Lens.HIGHLIGHTED_MTL);
};




Quiz_Lens.prototype.setHoveredObject = function (obj)
{
    // Clear old hovered object if a change is being made
    if (this.hoveredObj && this.hoveredObj != obj)
    {
        if (this.hoveredObj == this.selectedObj)
            this.hoveredObj.setMaterial(Quiz_Lens.SELECTED_MTL);
        else if (this.hoveredObj == this.highlightedObj)
            this.hoveredObj.setMaterial(Quiz_Lens.HIGHLIGHTED_MTL);
        else
            this.hoveredObj.resetMaterial();
    }

    // Set hovered object
    this.hoveredObj = obj;

    // Apply hover material
    if (this.hoveredObj)
        this.hoveredObj.setMaterial(Quiz_Lens.HOVERED_MTL);
};




Quiz_Lens.prototype.setSelectedObject = function (obj)
{
    // Clear old selected object if a change is being made
    if (this.selectedObj && this.selectedObj != obj)
    {
        if (this.selectedObj == this.highlightedObj)
            this.selectedObj.setMaterial(Quiz_Lens.HIGHLIGHTED_MTL);
        else
            this.selectedObj.resetMaterial();
    }

    // Set selected object
    this.selectedObj = obj;

    // Apply select material
    if (this.selectedObj)
        this.selectedObj.setMaterial(Quiz_Lens.SELECTED_MTL);
};
