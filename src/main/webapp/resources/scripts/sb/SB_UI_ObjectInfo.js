/**************************************************************************
    Class - SB Info Panel

    Shows information about scene under construction. Includes overview of
    scene and information about individual Scene Fragments

**************************************************************************/
var SB_UI_ObjectInfo = function (container, builder)
{
    this.container = container;
    this.builder = builder;

    // Embellish object as UI panel
    UI_Panel.embellish(this, "SB_UI_InfoPanel");

    this.htmlElements = {};

    this.buildUI();
};




// Create the UI for the picking panel
SB_UI_ObjectInfo.prototype.buildUI = function ()
{
    // Self reference
    var that = this;

    // Clear
    this.clear();

    if (this.selected)
    {
        // Component - Header
        var div_header = DOMUtils.div("name", "", "<b>Selected:</b> " + this.selected.getName());

        // Components - Style controls
        this.htmlElements.ctrl_ambient   = DOMUtils.input("Ambient",   "#" + this.selected.initialMtl.ambient.getHexString(),  "color", "width:62px");
        this.htmlElements.ctrl_diffuse   = DOMUtils.input("Diffuse",   "#" + this.selected.initialMtl.diffuse.getHexString(),    "color", "width:62px");
        this.htmlElements.ctrl_emissive  = DOMUtils.input("Emissive",  "#" + this.selected.initialMtl.emissive.getHexString(), "color", "width:62px");
        this.htmlElements.ctrl_specular  = DOMUtils.input("Specular",  "#" + this.selected.initialMtl.specular.getHexString(), "color", "width:62px");
        this.htmlElements.ctrl_shininess = DOMUtils.input_range("Shininess", this.selected.initialMtl.shininess, 1, 255, 1,             "width:62px");
        this.htmlElements.ctrl_alpha     = DOMUtils.input_range("Alpha", this.selected.initialMtl.alpha, 0, 1, 0.01,                "width:62px");
        this.htmlElements.ctrl_ambient.onchange     = function () { console.log("change! " + this); that._evt_objectStateChange(); };
        this.htmlElements.ctrl_diffuse.onchange     = function () { console.log("change! " + this); that._evt_objectStateChange(); };
        this.htmlElements.ctrl_emissive.onchange    = function () { console.log("change! " + this); that._evt_objectStateChange(); };
        this.htmlElements.ctrl_specular.onchange    = function () { console.log("change! " + this); that._evt_objectStateChange(); };
        this.htmlElements.ctrl_shininess.onchange   = function () { console.log("change! " + this); that._evt_objectStateChange(); };
        this.htmlElements.ctrl_alpha.onchange       = function () { console.log("change! " + this); that._evt_objectStateChange(); };

        // Component - Style control box
        var div_controls = DOMUtils.div("controls");
        div_controls.appendChild(DOMUtils.div("label", "", "Ambient"));
        div_controls.appendChild(DOMUtils.div("label", "", "Diffuse"));
        div_controls.appendChild(DOMUtils.div("label", "", "Emissive"));
        div_controls.appendChild(DOMUtils.div("label", "", "Specular"));
        div_controls.appendChild(DOMUtils.div("label", "", "Shininess"));
        div_controls.appendChild(DOMUtils.div("label", "", "Alpha"));
        div_controls.appendChild(DOMUtils.br());
        div_controls.appendChild(DOMUtils.div("control", null, null, null, this.htmlElements.ctrl_ambient));
        div_controls.appendChild(DOMUtils.div("control", null, null, null, this.htmlElements.ctrl_diffuse));
        div_controls.appendChild(DOMUtils.div("control", null, null, null, this.htmlElements.ctrl_emissive));
        div_controls.appendChild(DOMUtils.div("control", null, null, null, this.htmlElements.ctrl_specular));
        div_controls.appendChild(DOMUtils.div("control", null, null, null, this.htmlElements.ctrl_shininess));
        div_controls.appendChild(DOMUtils.div("control", null, null, null, this.htmlElements.ctrl_alpha));

        // Assemble
        this.container.appendChild(div_header);
        this.container.appendChild(DOMUtils.br());
        this.container.appendChild(div_controls);
    }
};




SB_UI_ObjectInfo.prototype._evt_objectStateChange = function ()
{
    var mtl = {
        ambient: this.htmlElements.ctrl_ambient.value,
        diffuse: this.htmlElements.ctrl_diffuse.value,
        emissive: this.htmlElements.ctrl_emissive.value,
        specular: this.htmlElements.ctrl_specular.value,
        shininess: this.htmlElements.ctrl_shininess.value,
        alpha: this.htmlElements.ctrl_alpha.value
    };

    this.builder.updateSceneFragmentMember_material(this.selected, mtl);
};




SB_UI_ObjectInfo.prototype.selectChange = function (data)
{
    this.selected = data.object;

    if (this.selected)
    {
        this.buildUI();
        $(this.container).show();
    }
    else
        $(this.container).hide();
};

