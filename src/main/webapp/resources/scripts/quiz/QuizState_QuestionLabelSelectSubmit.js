var QuizState_QuestionLabelSelectSubmit = function(quizApp)
{
    this.quizApp = quizApp;
    this.name = "Question Label Select Submit";
};




QuizState_QuestionLabelSelectSubmit.prototype.enter = function ()
{
    var answer = this.quizApp.uiElements.select_label.selectedOptions[0].innerHTML;

    var correct = this.quizApp.quizMaster.answer(answer);
    var question = this.quizApp.quizMaster.getCurrentQuestion();
    if (correct)
    {
        this.quizApp.queueStateChange(Quiz_Main.STATE_QUESTION_CORRECT);
        this.quizApp.communicator.sendStudentModelUpdate(question.factId, true);
    }
    else
    {
        this.quizApp.queueStateChange(Quiz_Main.STATE_QUESTION_SELECT_LABEL_INCORRECT);
        this.quizApp.communicator.sendStudentModelUpdate(question.factId, false);
    }
};




QuizState_QuestionLabelSelectSubmit.prototype.buildUI = function (panel)
{
    // Do nothing - this state has no UI
};
