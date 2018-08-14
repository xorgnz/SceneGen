var SG_TextCompletion = function () {};




SG_TextCompletion.applyTextCompletion = function(input)
{
    var that = this;

    // Apply text completion to marked fields
    $( input ).autocomplete({
        source: function( request, response )
        {
            $.ajax({
                url: that.serverURL + "textCompletion",
                dataType: "json",
                data:
                {
                    prefix: request.term,
                    limit: 100
                },
                success: function( data )
                {
                    console.log(data.completions);
                    response( data.completions );
                }
            });
        },
        minLength: 4,
    });
};
