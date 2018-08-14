/*
    Sim.js - A Simple Simulator for WebGL (based on Three.js)

    Originally written by Tony Parisi for "WebGL: Up and Running"
    Modified by Trond Nilsen

    Changes from base sim.js
     - Removed keyboard event handling. Doesn't work properly and is limited
     - Adjusted calls to App.handleMouseDown to pass event rather than just x,y
     - Adjusted calls to App.handleMouseMove to pass event rather than just x,y
     - Adjusted to use RayCaster instead of Ray (needed for compatibility with three.js r54
     - Split out interaction handling to separate classes
     - Removed some extraneous bits
     
     
*/

// Declare namespace
Sim = {};


/**************************************************************************
    Constants - Keyboard codes 
**************************************************************************/   
Sim.KeyCodes = {};
Sim.KeyCodes.KEY_SHIFT          = 16;
Sim.KeyCodes.KEY_ALT            = 18;
Sim.KeyCodes.KEY_LEFT           = 37; 
Sim.KeyCodes.KEY_UP             = 38;
Sim.KeyCodes.KEY_RIGHT          = 39;
Sim.KeyCodes.KEY_DOWN           = 40;
Sim.KeyCodes.KEY_C              = 67;
Sim.KeyCodes.KEY_R              = 82;
Sim.KeyCodes.KEY_V              = 86;
Sim.KeyCodes.KEY_QUESTION_MARK  = 191;




/**************************************************************************
    Class - Sim.App - application class 
    
    - Intended for use as a singleton
**************************************************************************/   
Sim.App = function()
{
	this.renderer = null;
	this.scene = null;
	this.camera = null;
	this.objects = [];
};


Sim.App.prototype.init = function(param)
{
    // Flesh out parameters with defaults
	param = param || {};
    param.camera_x = param.camera_x ? param.camera_x : 0;
    param.camera_y = param.camera_y ? param.camera_y : 0;
    param.camera_z = param.camera_z ? param.camera_z : 3.3333;
	
	// Store container
	this.container = param.container;
	this.container.setAttribute("tabindex", 1);
	
    // Create the Three.js renderer, add it to our div
    this.renderer = new THREE.WebGLRenderer( { antialias: true, canvas: param.canvas } );
    this.renderer.setSize(this.container.offsetWidth, this.container.offsetHeight);
    this.container.appendChild(this.renderer.domElement);
    this.renderer.domElement.setAttribute("tabindex", 1); // so it can take focus

    // Create a new Three.js scene
    this.scene = new THREE.Scene();
    this.scene.add( new THREE.AmbientLight( 0x505050 ) );
    this.scene.data = this;

    // Put in a camera at a good default location
    this.camera = new THREE.PerspectiveCamera( 45, this.container.offsetWidth / this.container.offsetHeight, 1, 10000 );
    this.camera.position.set( param.camera_x, param.camera_y, param.camera_z );
    this.scene.add(this.camera);
    
    // Create a root object to contain all other scene objects
    this.root = new THREE.Object3D();
    this.scene.add(this.root);
    
    // Create a projector to handle picking
    this.projector = new THREE.Projector();
    
    // Set up event handlers
    this.interactionManager = new Sim.InteractionManager(this, param);
    var that = this;
    window.addEventListener( 'resize', function(event) { that.onWindowResize(event); }, false );
};


// Add object to scene
Sim.App.prototype.addObject = function(obj)
{
    // Add to internal array of objects
	this.objects.push(obj);

	// If this is a renderable object, add it to the root scene
	if (obj.object3D)
		this.root.add(obj.object3D);
};


// Resize the renderer
Sim.App.prototype.onWindowResize = function(event) {

	this.renderer.setSize(this.container.offsetWidth, this.container.offsetHeight);

	this.camera.aspect = this.container.offsetWidth / this.container.offsetHeight;
	this.camera.updateProjectionMatrix();
};


// Remove object from scene
Sim.App.prototype.removeObject = function(obj)
{
	var index = this.objects.indexOf(obj);
	if (index != -1)
	{
	    // Remove from internal array of objects
		this.objects.splice(index, 1);
		
		// If this is a renderable object, remove it from the root scene
		if (obj.object3D)
			this.root.remove(obj.object3D);
	}
};


// Main loop
Sim.App.prototype.run = function()
{
    // Update scene
	this.update();
	
	// Render scene
	this.renderer.render( this.scene, this.camera );
	
	// Proceed to next frame
	var that = this;	
	requestAnimationFrame(function() 
    { 
        that.run(); 
    });	
};


// Update method - propogate down the tree
Sim.App.prototype.update = function()
{
	for (var i = 0; i < this.objects.length; i++)
	{
		this.objects[i].update();
	}
};


// Allow this element to receive focus
Sim.App.prototype.focus = function()
{
	if (this.renderer && this.renderer.domElement)
	{
		this.renderer.domElement.focus();
	}
};




/**************************************************************************
    Class - Sim.InteractionState
    
    - Listens for and propogates all mouse and keyboard events
**************************************************************************/   
// Constructor
Sim.InteractionState = function ()
{
    // Set initial mouse states 
    this.mouse = 
    { 
        x: 0, 
        y: 0,
        left: Sim.InteractionState.MOUSE_UP,
        middle: Sim.InteractionState.MOUSE_UP,
        right: Sim.InteractionState.MOUSE_UP
    };
    
    // Set persistent key states
    this.key_shift = Sim.InteractionState.KEY_UP;
};


// Constants
Sim.InteractionState.KEY_DOWN   = 201;
Sim.InteractionState.KEY_UP     = 202;
Sim.InteractionState.MOUSE_DOWN = 101;
Sim.InteractionState.MOUSE_UP   = 102;




/**************************************************************************
    Class - Sim.InteractionManager 
    
    - Listens for and propogates all mouse and keyboard events
**************************************************************************/   
// Constructor
Sim.InteractionManager = function (app, param)
{
    this.app = app;
    this.state = new Sim.InteractionState();
    
    // Enable picking by default
    this.flag_pickingOnHoverEnabled = param.pickingOnHoverEnabled ? param.pickingOnHoverEnabled : true;
    this.flag_pickingOnClickEnabled = param.pickingOnClickEnabled ? param.pickingOnClickEnabled : true;
    
    this.listeners = [];
    
    // Register for interaction events
    var that = this;
    this.app.renderer.domElement.addEventListener( 'mousemove',  function(e)  { that.onMouseMove(e); }, false );
    this.app.renderer.domElement.addEventListener( 'mousedown',  function(e)  { that.onMouseDown(e); }, false );
    this.app.renderer.domElement.addEventListener( 'mouseup',  function(e)    { that.onMouseUp(e); }, false );
    this.app.renderer.domElement.addEventListener( 'mouseleave', function (e) { that.onMouseLeaveCanvas(e); },  false );
    $(document).keydown(function (e) { that.onKeyDown(e); } );
    $(document).keyup(function (e) { that.onKeyUp(e); } );
	
    $(this.app.renderer.domElement).mousewheel(
        function(e, delta) {
            that.onMouseScroll(e, delta);
        }
    );
};


// Add a listener
Sim.InteractionManager.prototype.addListener = function (listener)
{
    this.listeners.push(listener);
};


// Remove all instances of this listener 
Sim.InteractionManager.prototype.removeListener = function (listener)
{
    for (var i = 0 ; i < this.listeners.length ; i++)
    {
        if (this.listeners[i] == listener)
            this.listeners[i] = null;
	}
};


// Pick the first object under mouse coordinates given
Sim.InteractionManager.prototype.objectFromMouse = function(x,y)
{
	// Translate page coords to element coords
	var offset = $(this.app.renderer.domElement).offset();
	var eltx = x - offset.left;
	var elty = y - offset.top;
	
	// Translate client coords into viewport x,y
    var vpx = ( eltx / this.app.container.offsetWidth ) * 2 - 1;
    var vpy = - ( elty / this.app.container.offsetHeight ) * 2 + 1;
    
    // Calculate vector pick vector
    var vector = new THREE.Vector3( vpx, vpy, 1.0 );
    this.app.projector.unprojectVector( vector, this.app.camera , 0, Infinity);
    vector.sub( this.app.camera.position );
    vector.normalize();
	
    // Cast ray to find intersects
    var raycaster = new THREE.Raycaster( this.app.camera.position, vector );
    var targets = this.app.objects[0].object3D.children;
    var intersects = raycaster.intersectObjects( targets, true );
	
    // Find first visible object
    for (var i = 0 ; i < intersects.length ; i ++)
    {
        if (intersects[i].object != null && intersects[i].object.visible)
        {
            var mat = new THREE.Matrix4().getInverse(intersects[i].object.matrixWorld);
            var point = intersects[i].point;
            point.applyMatrix4(mat); 
            return ({"obj":intersects[i].object, "point":point});
        }
    }
    
    // If none found, return empty pick    
    return {"obj":null, "point":null};
};


// Event - Key has been pressed while web browser has focus
Sim.InteractionManager.prototype.onKeyDown = function (e)
{
    // Update state
    if (e.keyCode == Sim.KeyCodes.KEY_SHIFT)
        this.state.key_shift = Sim.InteractionState.KEY_DOWN;
    
    // Notify listeners
    for (var i = 0 ; i < this.listeners.length ; i++)
        if (this.listeners[i])
            this.listeners[i].handleKeyDown(e, this.state);
};


// Event - Key has been released while web browser has focus
Sim.InteractionManager.prototype.onKeyUp = function (e)
{
    // Update state
    if (e.keyCode == Sim.KeyCodes.KEY_SHIFT)
        this.state.key_shift = Sim.InteractionState.KEY_UP;
    
    // Notify listeners
    for (var i = 0 ; i < this.listeners.length ; i++)
        if (this.listeners[i])
            this.listeners[i].handleKeyUp(e, this.state);
};
    

// Event - mouse has been clicked within webGL canvas
Sim.InteractionManager.prototype.onMouseDown = function(event)
{
    event.preventDefault();
    this.app.renderer.domElement.focus();
    
    // Update state
    this.state.mouse.x = event.pageX;
    this.state.mouse.y = event.pageY;

    if (event.button == 0)
        this.state.mouse.left = Sim.InteractionState.MOUSE_DOWN;
    
    if (event.button == 1)
        this.state.mouse.middle = Sim.InteractionState.MOUSE_DOWN;
    
    if (event.button == 2)
        this.state.mouse.right = Sim.InteractionState.MOUSE_DOWN;
    
    // Handle picking
    if (this.flag_pickingOnClickEnabled && event.button == 0)
    {
        // Pick        
        var pickResult = this.objectFromMouse(event.pageX, event.pageY);
        
        // Call controller
        if (pickResult.obj)
        {
            for (var i = 0 ; i < this.listeners.length ; i++)
                if (this.listeners[i])
                    this.listeners[i].handleObjectClicked(pickResult.obj.parent.data);
        }
        else
        {
            for (var i = 0 ; i < this.listeners.length ; i++)
                if (this.listeners[i])
                    this.listeners[i].handleObjectClicked(null);
        }
    }
    
    for (var i = 0 ; i < this.listeners.length ; i++)
        if (this.listeners[i])
            this.listeners[i].handleMouseDown(this.state);
};


// Event - mouse has left webGL canvas
Sim.InteractionManager.prototype.onMouseLeaveCanvas = function(event)
{
    this.state.mouse.left = Sim.InteractionState.MOUSE_UP;
    this.state.mouse.middle = Sim.InteractionState.MOUSE_UP;
    this.state.mouse.right = Sim.InteractionState.MOUSE_UP;
    
    for (var i = 0 ; i < this.listeners.length ; i++)
        if (this.listeners[i])
            this.listeners[i].handleMouseLeave();
};


// Event - mouse has moved within webGL canvas
Sim.InteractionManager.prototype.onMouseMove = function(event)
{
    event.preventDefault();
    
    // Update state
    this.state.mouse.x = event.pageX;
    this.state.mouse.y = event.pageY;
    
    // Handle picking (if enabled)
    if (this.flag_pickingOnHoverEnabled)
    {
        var pickResult = this.objectFromMouse(event.pageX, event.pageY);
        
        // Call controller
        if (pickResult.obj)
        {
            for (var i = 0 ; i < this.listeners.length ; i++)
                if (this.listeners[i])
                    this.listeners[i].handleObjectHovered(pickResult.obj.parent.data);
        }
        else
        {
            for (var i = 0 ; i < this.listeners.length ; i++)
                if (this.listeners[i])
                    this.listeners[i].handleObjectHovered(null);
        }
    }
                
    // Notify listeners of mouse motion
    for (var i = 0 ; i < this.listeners.length ; i++)
        if (this.listeners[i])
            this.listeners[i].handleMouseMove(this.state);
};


// Event - mouse wheel has been scrolled within webGL canvas
Sim.InteractionManager.prototype.onMouseScroll = function(event, delta)
{
    event.preventDefault();

    // Notify listeners
    for (var i = 0 ; i < this.listeners.length ; i++)
        if (this.listeners[i])
    	   this.listeners[i].handleMouseScroll(delta);
};


// Event - mouse has been released within webGL canvas
Sim.InteractionManager.prototype.onMouseUp = function(event)
{
    event.preventDefault();
    
    if (event.button == 0)
        this.state.mouse.left = Sim.InteractionState.MOUSE_UP;
    
    if (event.button == 1)
        this.state.mouse.middle = Sim.InteractionState.MOUSE_UP;
    
    if (event.button == 2)
        this.state.mouse.right = Sim.InteractionState.MOUSE_UP;
    
    for (var i = 0 ; i < this.listeners.length ; i++)
        if (this.listeners[i])
            this.listeners[i].handleMouseUp(this.state);
};




/**************************************************************************
    Class - Sim.Object - base class for all objects in our simulation
**************************************************************************/   
Sim.Object = function()
{
	this.object3D = null;
	this.children = [];
};

Sim.Object.prototype.update = function()
{
	this.updateChildren();
};

// setPosition - move the object to a new position
Sim.Object.prototype.setPosition = function(x, y, z)
{
	if (this.object3D)
	{
		this.object3D.position.set(x, y, z);
	}
};

//setScale - scale the object
Sim.Object.prototype.setScale = function(x, y, z)
{
	if (this.object3D)
	{
		this.object3D.scale.set(x, y, z);
	}
};

//setScale - scale the object
Sim.Object.prototype.setVisible = function(visible)
{
	function setVisible(obj, visible)
	{
		obj.visible = visible;
		var i, len = obj.children.length;
		for (i = 0; i < len; i++)
		{
			setVisible(obj.children[i], visible);
		}
	}
	
	if (this.object3D)
	{
		setVisible(this.object3D, visible);
	}
};

// updateChildren - update all child objects
Sim.Object.prototype.update = function()
{
	var i, len;
	len = this.children.length;
	for (i = 0; i < len; i++)
	{
		this.children[i].update();
	}
};

Sim.Object.prototype.setObject3D = function(object3D)
{
	object3D.data = this;
	this.object3D = object3D;
};

//Add/remove children
Sim.Object.prototype.addChild = function(child)
{
	this.children.push(child);
	
	// If this is a renderable object, add its object3D as a child of mine
	if (child.object3D)
	{
		this.object3D.add(child.object3D);
	}
};

Sim.Object.prototype.removeChild = function(child)
{
	var index = this.children.indexOf(child);
	if (index != -1)
	{
		this.children.splice(index, 1);
		// If this is a renderable object, remove its object3D as a child of mine
		if (child.object3D)
		{
			this.object3D.remove(child.object3D);
		}
	}
};

// Some utility methods
Sim.Object.prototype.getScene = function()
{
	var scene = null;
	if (this.object3D)
	{
		var obj = this.object3D;
		while (obj.parent)
		{
			obj = obj.parent;
		}
		
		scene = obj;
	}
	
	return scene;
};

Sim.Object.prototype.getApp = function()
{
	var scene = this.getScene();
	return scene ? scene.data : null;
};
