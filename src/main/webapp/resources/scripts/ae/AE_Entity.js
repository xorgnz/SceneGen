var AE_Entity = function (id, name, filename, link, style, data)
{
    // Validate / Set default values
    if (id === null) throw "Cannot create SceneObject with null ID";
    name = name ? name : "Anatomical Entity";
    filename = filename ? filename : "";
    link = link ? link : "http://www.wikipedia.org/" + this.name;
    data = data ? data : {};

    // Store values
    this.id = id;
    this.name = name;
    this.filename = filename;
    this.link = link;
    this.data = data;

    // Store style. If no style given, use default
    this.style = {};
    if (style)
    {
        this.style.ambient   = style.ambient;
        this.style.diffuse   = style.diffuse;
        this.style.emissive  = style.emissive;
        this.style.specular  = style.specular;
        this.style.shininess = style.shininess;
        this.style.alpha     = style.alpha;
    }
    else
    {
        this.style.ambient   = "#808080";
        this.style.diffuse   = "#808080";
        this.style.emissive  = "#000000";
        this.style.specular  = "#ffffff";
        this.style.shininess = "20";
        this.style.alpha     = 1;
    }
};
