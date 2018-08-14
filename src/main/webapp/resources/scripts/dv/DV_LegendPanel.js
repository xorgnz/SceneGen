/**************************************************************************
    Class - AE Focus Panel

    Shows information concerning current exploration focus
    Provides UI controls to shift focus

**************************************************************************/
var DV_LegendPanel = function (container, sceneManager)
{
    // Components
    this.container = container;
    this.sceneManager = sceneManager;

    // Embellish object as UI panel
    UI_Panel.embellish(this, "DV_LegendPanel");

    // Create UI Elements
    this.htmlElements = {};

    // Create renderer
    this.renderer = new THREE.WebGLRenderer();
	this.renderer.setSize( 60, 40 );

    this.buildUI();
};




// Create the UI for the picking panel
DV_LegendPanel.prototype.buildUI = function ()
{
    // Clear panel
    this.clear();
    this.htmlElements.swatches = new Array();

    // Component - Title
    var div_header = DOMUtils.header(1, "Legend");
    this.container.appendChild(div_header);

    // Extract map of styles in use from scene
    var styleMap = {};
    var objects = this.sceneManager.getObjectData();
    for (var i = 0 ; i < objects.length ; i ++)
        if (objects[i].style)
            styleMap[objects[i].style.id] = objects[i].style;

    // Create sorted list of styles
    var styles = [];
    for (var id in styleMap)
        styles.push(styleMap[id]);
    styles = _.sortBy(styles, function (o) { return o.tag; });

    // Create swatches and labels
    for (var i = 0 ; i < styles.length ; i++)
    {
        var swatch = new DV_LegendPanel_Swatch(styles[i], this.renderer);
        this.htmlElements.swatches.push(swatch);
        this.container.appendChild(swatch.getDiv());
        this.container.appendChild(DOMUtils.div("label", "", styles[i].tag));
    }

    if (this.htmlElements.swatches.length == 0)
        this.container.appendChild(DOMUtils.div("", "", "No styles visible"));

    this.enabled = false;
	this.frame();
};




DV_LegendPanel.prototype.frame = function ()
{
    var that = this;

    for (var i = 0 ; i < this.htmlElements.swatches.length ; i++)
        this.htmlElements.swatches[i].animateAndRender();

    requestAnimationFrame(function()
    {
        if (that.enabled)
            that.frame();
    });
};




var DV_LegendPanel_Swatch = function (mtl, renderer)
{
    // DOM Element
    this.div = DOMUtils.div("swatch");

    // Create scene
	this.scene = new THREE.Scene();
	this.renderer = renderer;

    // Create camera
	this.camera = new THREE.PerspectiveCamera( 45, 60 / 40, 1, 1000 );
	this.camera.position.set(130, 130, 130);
	this.camera.lookAt(new THREE.Vector3(0, 0, 0));
	this.scene.add(this.camera);

	 // Create Scene
    var geometry = new THREE.CubeGeometry(100,100,100);
    var material = new THREE.MeshPhongMaterial({
        ambient: mtl.ambient,
        color: mtl.diffuse,
        specular: mtl.specular,
        emissive: mtl.emissive,
        shininess: mtl.shininess,
        shading: THREE.SmoothShading,
        blending: THREE.AdditiveBlending,
        transparent: mtl.alpha != 1,
        opacity: mtl.alpha,
    });
	this.mesh = new THREE.Mesh( geometry, material);
	this.mesh.position.set(0, 0, 0);
	this.scene.add( this.mesh );

    // Create ambient light
    this.scene.add( new THREE.AmbientLight( 0x505050 ) );

    // Create point light
    this.light = new THREE.PointLight( 0xffffff, 1, 5000 );
    this.light.position.set( 90, 90, 90 );
    this.scene.add(this.light);
};




DV_LegendPanel_Swatch.prototype.getDiv = function ()
{
    return this.div;
};




DV_LegendPanel_Swatch.prototype.animateAndRender = function ()
{
    // var time = Date.now() * 0.0005;
    var time = 2.5;

    if (this.light)
    {
        this.light.position.y = 90;
        this.light.position.x = Math.sin( time ) * 90;
        this.light.position.z = Math.cos( time ) * 90;
    }

    // Render to graphics context
	this.renderer.render( this.scene, this.camera );

	// Apply graphics context as swatch image content
	var dataURL = this.renderer.domElement.toDataURL();
	this.div.style.background = "url('" + dataURL + "')";
};
