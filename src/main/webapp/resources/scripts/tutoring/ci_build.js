    function addEntityKnowledge()
    {
        var div_message = document.getElementById("entityMessage");
        div_message.innerHTML = "Working ...";

        // Compose URL
        var url = serverURL + "tutoring/curriculumItem/" + ciId + "/build/addEntityKnowledge";

        // Retrieve parameters from select
        var select_add = document.getElementById("entityAddSelect");
        if (select_add.selectedOptions.length == 0)
        {
            console.log("BuildCI.addEntityKnowledge - abort - no selected option");
            return;
        }
        var option_add = select_add.selectedOptions[0];

        // Compose ajax data packet
        var ajaxData =
        {
            entityId: option_add.value,
            entityLabel: option_add.text,
        };

        changed = true;

        console.log("BuildCI.addEntityKnowledge - invoke: " + url);
        console.log(ajaxData);

        // Send request
        $.ajax({
            url: url,
            dataType: 'json',
            data: ajaxData,
            type: 'post'
        })

        // Process response
        .done(function ( data )
        {
            if (data.success)
            {
                console.log("BuildCI.addEntityKnowledge - response:");
                console.log(data);

                // Determine whether entity is listed in removal select box
                var select_remove = document.getElementById("entityRemoveSelect");
                var found = false;
                for (var i = 0 ; i < select_remove.length ; i++)
                	if (select_remove[i].value == data.nodeId)
                		found = true;

                // If not found, add entity to removal select box
                if (!found)
                {
                	var option_remove = DOMUtils.option(data.nodeId, option_add.text);
                	select_remove.appendChild(option_remove);

                    div_message.innerHTML = "";
                }
            }
            else
            {
                alert(data.message);
            }
        });
    }

    function addRelationKnowledge()
    {
        var div_message = document.getElementById("relationMessage");
        div_message.innerHTML = "Working ...";

        // Compose URL
        var url = serverURL + "tutoring/curriculumItem/" + ciId + "/build/addRelationKnowledge";

        // Retrieve parameters from select
        var select_add = document.getElementById("relationAddSelect");
        if (select_add.selectedOptions.length == 0)
        {
            console.log("BuildCI.addRelationKnowledge - abort - no selected option");
            return;
        }
        var option_add = select_add.selectedOptions[0];

        // Compose ajax data packet
        var ajaxData =
        {
            namespace: option_add.value.split(":")[0],
            name: option_add.value.split(":")[1],
        };

        changed = true;

        console.log("BuildCI.addRelationKnowledge - invoke: " + url);
        console.log(ajaxData);

        // Send request
        $.ajax({
            url: url,
            dataType: 'json',
            data: ajaxData,
            type: 'post'
        })

        // Process response
        .done(function ( data )
        {
            if (data.success)
            {
                console.log("BuildCI.addRelationKnowledge - response:");
                console.log(data);

                var select_remove = document.getElementById("relationRemoveSelect");
                var option_remove = DOMUtils.option(data.nodeId, option_add.text);
                select_remove.appendChild(option_remove);

                div_message.innerHTML = "";
            }
            else
            {
                alert(data.message);
            }
        });
    }

    function removeEntityKnowledge()
    {
        var div_message = document.getElementById("entityMessage");
        div_message.innerHTML = "Working ...";

        // Compose URL
        var url = serverURL + "tutoring/curriculumItem/" + ciId + "/build/removeEntityKnowledge";

        // Retrieve select widget
        var select = document.getElementById("entityRemoveSelect");
        if (select.selectedOptions.length == 0)
        {
            console.log("BuildCI.removeRelationKnowledge - abort - no selected option");
            return;
        }

        // Compose ajax data packet
        var ajaxData =
        {
            nodeIds: [],
        };
        for (var i = 0 ; i < select.selectedOptions.length ; i ++)
            ajaxData.nodeIds.push(select.selectedOptions[i].value);

        changed = true;

        // Send request
        console.log("BuildCI.removeRelationKnowledge - invoke: " + url);
        console.log($.param(ajaxData,true));
        $.ajax({
            url: url,
            dataType: 'json',
            data: ajaxData,
            type: 'post',
            traditional: true
        })

        // Process response
        .done(function ( data )
        {
            if (data.success)
            {
                console.log("BuildCI.removeEntityKnowledge - response:");
                console.log(data);

                // Make array of options to remove from select widget
                var toRemove = [];
                for (var i = 0 ; i < select.selectedOptions.length ; i ++)
                    toRemove.push(select.selectedOptions[i]);

                // Remove options from select widget
                for (var i = 0 ; i < toRemove.length ; i ++)
                    select.removeChild(toRemove[i]);

                div_message.innerHTML = "";
            }
            else
            {
                alert(data.message);
            }
        });
    }

    function removeRelationKnowledge()
    {
        var div_message = document.getElementById("relationMessage");
        div_message.innerHTML = "Working ...";

        // Compose URL
        var url = serverURL + "tutoring/curriculumItem/" + ciId + "/build/removeRelationKnowledge";

        // Retrieve select widget
        var select = document.getElementById("relationRemoveSelect");
        if (select.selectedOptions.length == 0)
        {
            console.log("BuildCI.removeRelationKnowledge - abort - no selected option");
            return;
        }

        // Compose ajax data packet
        var ajaxData =
        {
            nodeIds: [],
        };
        for (var i = 0 ; i < select.selectedOptions.length ; i ++)
            ajaxData.nodeIds.push(select.selectedOptions[i].value);

        changed = true;

        // Send request
        console.log("BuildCI.removeRelationKnowledge - invoke: " + url);
        console.log($.param(ajaxData,true));
        $.ajax({
            url: url,
            dataType: 'json',
            data: ajaxData,
            type: 'post',
            traditional: true
        })

        // Process response
        .done(function ( data )
        {
            if (data.success)
            {
                console.log("BuildCI.removeRelationKnowledge - response:");
                console.log(data);

                // Make array of options to remove from select widget
                var toRemove = [];
                for (var i = 0 ; i < select.selectedOptions.length ; i ++)
                    toRemove.push(select.selectedOptions[i]);

                // Remove options from select widget
                for (var i = 0 ; i < toRemove.length ; i ++)
                    select.removeChild(toRemove[i]);

                div_message.innerHTML = "";
            }
            else
            {
                alert(data.message);
            }
        });
    }


    function toggleLabelKnowledge()
    {
    	var div_message = document.getElementById("labelMessage");
    	div_message.innerHTML = "Working ...";

        // Compose URL
        var url = serverURL + "tutoring/curriculumItem/" + ciId + "/build/toggleLabelKnowledge";

        // Retrieve select widget
        var select = document.getElementById("labelToggleSelect");
        if (select.selectedOptions.length == 0)
        {
            console.log("BuildCI.toggleLabelKnowledge - abort - no selected option");
            return;
        }
        var included = select.selectedOptions[0].value;

        console.log(included);

        // Compose ajax data packet
        var ajaxData =
        {
            included: included
        };

        changed = true;

        // Send request
        console.log("BuildCI.toggleLabelKnowledge - invoke: " + url);
        console.log($.param(ajaxData,true));
        $.ajax({
            url: url,
            dataType: 'json',
            data: ajaxData,
            type: 'post',
            traditional: true
        })

        // Process response
        .done(function ( data )
        {
            if (data.success)
            {
                console.log("BuildCI.toggleLabelKnowledge - response:");
                console.log(data);

                if (included == 'true')
                    div_message.innerHTML = "Label knowledge included";
                else
                	div_message.innerHTML = "Label knowledge not included";
            }
            else
            {
                alert(data.message);
            }
        });
    }


    var changed = false;
    $(document).ready(function ()
    {
        window.onbeforeunload = function()
        {
            if (changed)
            {
            	console.log("Dispatching changed event");

                var url = serverURL + "/tutoring/curriculumItem/" + ciId + "/build/createFactNodes";

                $.ajax({
                    url: url,
                    dataType: 'json',
                    type: 'post',
                });

                return;
            }
        };
    });