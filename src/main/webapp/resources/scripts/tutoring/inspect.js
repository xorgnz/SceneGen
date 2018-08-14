var Node = function (n)
{
    // Store parameters
    this.name = n.name;
    this.id = n.id;
    this.type = n.type;

    this.p = n.p;

    // Flag whether a node can have children
    if (this.type == Node.TYPE_RFACT)
        this.canHaveChildren = false;
    else
        this.canHaveChildren = true;

    // Create children
    this.items = null;
    if (typeof n.items != 'undefined' && n.items != null && this.type != Node.TYPE_RFACT && n.items.length > 0)
    {
        n.items = _.sortBy(n.items, "name");
        
        this.items = [];

        // Add label facts
        for (var i = 0 ; i < n.items.length ; i ++)
        {
            if (n.items[i] && n.items[i].type == Node.TYPE_LFACT)
                this.items.push(new Node(n.items[i]));
        }
        
        // Add relation facts
        for (var i = 0 ; i < n.items.length ; i ++)
        {
            if (n.items[i] && n.items[i].type == Node.TYPE_RFACT)
                this.items.push(new Node(n.items[i]));
        }
        
        // Add other nodes
        for (var i = 0 ; i < n.items.length ; i ++)
        {
            if (n.items[i] && n.items[i].type != Node.TYPE_RFACT && n.items[i].type != Node.TYPE_LFACT)
                this.items.push(new Node(n.items[i]));
        }
    }

    // Define HTML references
    this.button_toggle = null;
    this.div_children = null;
};
Node.TYPE_CURRICULUM         = 0;
Node.TYPE_CURRICULUM_ITEM    = 1;
Node.TYPE_ENTITY_KNOWLEDGE   = 2;
Node.TYPE_LABEL_KNOWLEDGE    = 3;
Node.TYPE_RELATION_KNOWLEDGE = 4;
Node.TYPE_LFACT              = 5;
Node.TYPE_RFACT              = 6;


Node.prototype.express = function(div_container)
{
    // Self reference
    var that = this;

    // Create own row
    var div_element = DOMUtils.div("hier_element");
    
    if (this.type == Node.TYPE_CURRICULUM)
    	div_element.className += " c";
    else if (this.type == Node.TYPE_CURRICULUM_ITEM)
    	div_element.className += " ci";
    else if (this.type == Node.TYPE_ENTITY_KNOWLEDGE)
    	div_element.className += " ek";
    else if (this.type == Node.TYPE_LABEL_KNOWLEDGE)
    	div_element.className += " lk";
    else if (this.type == Node.TYPE_RELATION_KNOWLEDGE)
    	div_element.className += " rk";
    else if (this.type == Node.TYPE_LFACT)
    	div_element.className += " lfact";
    else if (this.type == Node.TYPE_RFACT)
    	div_element.className += " rfact";
    
    var span_label = DOMUtils.span("hier_label", null, this.name);

    // Create links box
    var div_links = DOMUtils.div("links_holder");
    if (this.type == Node.TYPE_RFACT)
	{
	    var a_true = DOMUtils.link("tutoring/studentModel/rFact/" + this.id + "/b/" + studentId + "/true", "True");
	    var a_false = DOMUtils.link("tutoring/studentModel/rFact/" + this.id + "/b/" + studentId + "/false", "False");
	    div_links.appendChild(a_true);
	    div_links.appendChild(document.createTextNode(" "));
	    div_links.appendChild(a_false);
	}
    else if (this.type == Node.TYPE_LFACT)
	{
	    var a_true = DOMUtils.link("tutoring/studentModel/lFact/" + this.id + "/b/" + studentId + "/true", "True");
	    var a_false = DOMUtils.link("tutoring/studentModel/lFact/" + this.id + "/b/" + studentId + "/false", "False");
	    div_links.appendChild(a_true);
	    div_links.appendChild(document.createTextNode(" "));
	    div_links.appendChild(a_false);
	}

    // Create progress bar
    var p = (this.p * 100).toFixed(2);
    var pinv = ((1 - this.p) * 100).toFixed(2);
    var div_pb = DOMUtils.div("pb_holder");
    var div_pb_label = DOMUtils.div("pb_label", "", p + "%");
    var div_pb_bars = DOMUtils.div("pb_bars");
    var div_pb_bar_left = DOMUtils.div("pb_bar_left", "", "&#160;");
    div_pb_bar_left.style.width = p + "%";
    var div_pb_bar_right = DOMUtils.div("pb_bar_right", "", "&#160;");
    div_pb_bar_right.style.width = pinv + "%";

    // Assemble progress bar
    div_pb.appendChild(div_pb_label);
    div_pb_bars.appendChild(div_pb_bar_left);
    div_pb_bars.appendChild(div_pb_bar_right);
    div_pb.appendChild(div_pb_bars);

    if (this.items == null)
    {
        // Assemble - simple row
        div_element.appendChild(span_label);
        div_element.appendChild(div_links);
        div_element.appendChild(div_pb);
        div_container.appendChild(div_element);
    }
    else
    {
        // Create toggle button
        this.button_toggle = DOMUtils.button("hier_toggle_open", null, null, function () { that.toggleChildren(); });

        // Create container for children
        this.div_children = DOMUtils.div("hier_children");
        this.div_children.style.display = "";

        // Create children if loaded
        if (this.items != null)
        {
            for (var i = 0 ; i < this.items.length ; i ++)
                this.items[i].express(this.div_children);
        }

        // Assemble
        div_element.appendChild(this.button_toggle);
        div_element.appendChild(span_label);
        div_element.appendChild(div_pb);
        div_container.appendChild(div_element);
        div_container.appendChild(this.div_children);
    }
};


Node.prototype.toggleChildren = function()
{
    var that = this;
    console.log(that);

    // Toggle button & children panel display
    if (this.items != null)
    {
        if (this.button_toggle.className == "hier_toggle_closed")
        {
            this.button_toggle.className = "hier_toggle_open";
            this.div_children.style.display = "";
        }
        else
        {
            this.button_toggle.className = "hier_toggle_closed";
            this.div_children.style.display = "none";
        }
    }
};
