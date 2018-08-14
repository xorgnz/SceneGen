// Storage for parameter rows
var rows = [];




var addParameter = function ()
{   
    createParameterLine({
        variable: "",
        label: "",
        flag_entityId: false,
        flag_entityName: false,
    });

};




// Add a parameter row
var createParameterLine = function (parameter)
{
    // Build - row
    var tbody = document.getElementById("tbody-params");
    var tr = document.createElement("tr");
    tbody.insertBefore(tr, tbody.lastChild);

    // Component - Label
    var input_label = DOMUtils.input("pl", parameter.label ? parameter.label : "", "text", "width:233px");
    var td_label = document.createElement("td");
    td_label.appendChild(input_label);
    
    // Component - Variable
    var input_variable = DOMUtils.input("pv", parameter.variable ? parameter.variable : "", "text", "width:233px");
    var td_variable = document.createElement("td");
    td_variable.appendChild(input_variable);
    
    // Component - Flags - Entity ID
    var checkbox_flag_entityId = DOMUtils.input("pfei", null, "checkbox");
    if (parameter.flag_entityId) checkbox_flag_entityId.checked = true;
    
    // Component - Flags - Entity Name
    var checkbox_flag_entityName = DOMUtils.input("pfen", null, "checkbox");
    if (parameter.flag_entityName) checkbox_flag_entityName.checked = true;
    
    // Assemble - Flags
    var td_flags = document.createElement("td");
    td_flags.appendChild(DOMUtils.text("Entity ID: "));
    td_flags.appendChild(checkbox_flag_entityId);
    td_flags.appendChild(DOMUtils.br());
    td_flags.appendChild(DOMUtils.text("Entity Name: "));
    td_flags.appendChild(checkbox_flag_entityName);

    // Component - Button
    var td_buttons = document.createElement("td");  
    var button = DOMUtils.input("", "Delete", "button");
    button.onclick = function () 
    { 
        tbody.removeChild(tr);
        
        console.log(rows);
        
        _.remove(rows, tr);
        
        console.log(rows);
    };
    td_buttons.appendChild(button); 
    
    // Assemble
    tr.appendChild(td_variable);
    tr.appendChild(td_label);   
    tr.appendChild(td_flags);   
    tr.appendChild(td_buttons);

    // Store fields for easy access later
    tr.fields = 
    {
        variable: input_variable,
        label: input_label,
        flag_entityId: checkbox_flag_entityId,
        flag_entityName: checkbox_flag_entityName,
    };
    
    // Store row for easy access
    rows.push(tr);
};




var submitForm = function ()
{
    var tbody = document.getElementById("tbody-params");
    
    var variables = {};
    
    for (var i = 0 ; i < rows.length ; i++ )
    {
        // Proceed if row has fields, and variable is set
        var fields = rows[i].fields;
        if (fields && fields.variable.value)
        {
            if (! variables[fields.variable.value])
            {
                // Mark this variable name as used
                variables[fields.variable.value] = true;
                
                // Field - Variable
                var hidden_variable = DOMUtils.input("param_label", fields.label.value, "hidden");
                tbody.appendChild(hidden_variable);
                
                // Field - Label
                var hidden_label = DOMUtils.input("param_variable", fields.variable.value, "hidden");
                tbody.appendChild(hidden_label);
                
                // Field - Flag - Entity ID
                if (fields.flag_entityId.checked)
                {
                    var hidden_flag_entityId = DOMUtils.input("param_flag_entityId", fields.variable.value, "hidden");
                    tbody.appendChild(hidden_flag_entityId);
                }
                
                // Field - Flag - Entity Name
                if (fields.flag_entityName.checked)
                {
                    var hidden_flag_entityName = DOMUtils.input("param_flag_entityName", fields.variable.value, "hidden");
                    tbody.appendChild(hidden_flag_entityName);
                }
            }
        }
    }
};