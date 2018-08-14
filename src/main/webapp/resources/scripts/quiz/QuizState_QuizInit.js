var QuizState_QuizInit = function(quizApp)
{
    this.quizApp = quizApp;
    this.name = "Quiz Init";
};




QuizState_QuizInit.prototype.enter = function ()
{
    this.quizApp.quizMaster.clear();
    this.quizApp.quizMaster.generateQuiz(this.quizApp.config.quizLength);

    this.quizApp.queueStateChange(Quiz_Main.STATE_QUESTION_INIT);
};




QuizState_QuizInit.prototype.buildUI = function (panel)
{
    // Do nothing - this state has no UI.
};
