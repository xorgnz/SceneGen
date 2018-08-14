var SB_UI_LinkPanel = function(container, config)
{
    // Initialize member fields
    this.container = container;
    this.config = config;

    // Validate
    if (this.container == null)
        alert("Cannot create SB_UI_LinkPanel - missing div");

    // Embellish object as UI panel
    UI_Panel.embellish(this, "SB_UI_LinkPanel");
};




// Create user interface elements for this load progress panel.
// Called at the beginning of any load sequence to create the panel
SB_UI_LinkPanel.prototype.buildUI = function ()
{
    // Clear
    this.clear();

    var url_view = this.config.serverURL + "scene/" + this.config.sceneId;
    var url_zip = this.config.serverURL + "scene/" + this.config.sceneId + "/zip";

    var html = "";
    html += "<h1>Using this scene</h1>";
    html += "<h2>On our server</h2>";
    html += "Use the following URL to view the scene on our server. Note that our server is primarily intended ";
    html += "for development, and may be insufficiently reliable for your needs.<br/>";
    html += "<b>URL:</b> <a href='" + url_view + "'>" + url_view + "<a/>";
    html += "<h2>On your own server</h2>";
    html += "Download the scene from the following URL and deploy it on your own server. Note that models will not load ";
    html += "correctly if you load the scene as an HTML file from your desktop.<br/>";
    html += "<b>URL:</b> <a href='" + url_zip + "'>" + url_zip + "<a/>";
    html += "<h2>Embedding the scene</h2>";
    html += "A scene may be embedded into an existing web page using an HTML <span class=\"code\">iframe</span> element. ";
    html += "Use the following code, replacing <span class=\"code\">##URL##</span> with the URL for the scene on either ";
    html += "our server or yours, and <span class=\"code\">##x##</span> ";
    html += "and <span class=\"code\">##y##</span> for the width and height you wish the scene to occupy, respectively.";
    html += '<textarea><iframe width="##x##" height="##y##" src="##URL##" frameborder="0"></iframe></textarea>';

    // Assemble
    this.container.innerHTML = html;
};
