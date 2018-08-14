var QuizState_QuestionModelSelectSubmit = function(quizApp)
{
    this.quizApp = quizApp;
    this.name = "Question Model Select Submit";
};




QuizState_QuestionModelSelectSubmit.prototype.enter = function ()
{
    var selected = this.quizApp.controller.lens.selectedObj;

    if (selected == null)
        alert("No object selected! Select one, and try again");
    else
    {
        var correct = this.quizApp.quizMaster.answer(selected.data.id);
        var question = this.quizApp.quizMaster.getCurrentQuestion();
        if (correct)
        {
            this.quizApp.queueStateChange(Quiz_Main.STATE_QUESTION_CORRECT);
            this.quizApp.communicator.sendStudentModelUpdate(question.factId, true);
        }
        else
        {
            this.quizApp.queueStateChange(Quiz_Main.STATE_QUESTION_SELECT_MODEL_INCORRECT);
            this.quizApp.communicator.sendStudentModelUpdate(question.factId, false);
        }
    }
};




QuizState_QuestionModelSelectSubmit.prototype.buildUI = function (panel)
{
    // Do nothing - this state has no UI
};
