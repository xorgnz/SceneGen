var Quiz_Communicator = function(serverURL, studentId)
{
    this.serverURL = serverURL;
    this.studentId = studentId;
};


Quiz_Communicator.prototype.sendStudentModelUpdate = function(factId, correct)
{
    $.ajax({
        url: this.serverURL + "/tutoring/REST/studentModel/lFact/update",
        dataType: 'json',
        data:
        {
            factId: factId,
            studentId: this.studentId,
            correct: correct,
        },
        type: 'post'
    })

    // Process response
    .done(function ( data )
    {
        console.log("Quiz_Communicator.sendStudentModelUpdate - response: " + (data.success ? "success" : "fail") + " " + data.message);
    });
};

