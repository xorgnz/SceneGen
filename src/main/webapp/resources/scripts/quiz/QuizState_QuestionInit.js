var QuizState_QuestionInit = function(quizApp)
{
    this.quizApp = quizApp;
    this.name = "Question Init";
};




QuizState_QuestionInit.prototype.enter = function ()
{
    // Move to next question
    this.quizApp.quizMaster.nextQuestion();

    // Clear lens
    this.quizApp.controller.lens.clear();

    // Re-display full scene
    this.quizApp.cgaApp.gfxEngine.camera.applyStoredConfiguration();
    var objects = this.quizApp.cgaApp.gfxEngine.scene.getObjects();
    for (var i in objects)
        objects[i].setVisibility(true);

    // If questions remain, display next one.
    if (!this.quizApp.quizMaster.isFinished())
    {
        if (this.quizApp.quizMaster.getCurrentQuestion().direction == Quiz_QuizMaster.DIRECTION_SELECT_LABEL)
            this.quizApp.queueStateChange(Quiz_Main.STATE_QUESTION_SELECT_LABEL);
        else
            this.quizApp.queueStateChange(Quiz_Main.STATE_QUESTION_SELECT_MODEL);
    }

    // Or, end the quiz
    else
        this.quizApp.queueStateChange(Quiz_Main.STATE_SUMMARY);

};




QuizState_QuestionInit.prototype.buildUI = function (panel)
{
    // Do nothing - this state has no UI.
};
