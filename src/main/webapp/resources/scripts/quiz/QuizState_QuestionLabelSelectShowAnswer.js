QuizState_QuestionLabelSelectShowAnswer = function(quizApp)
{
    this.quizApp = quizApp;
    this.name = "Question Select Label Show Answer";
};




QuizState_QuestionLabelSelectShowAnswer.prototype.enter = function()
{
    var answer = this.quizApp.quizMaster.getCurrentQuestion();
    this.quizApp.controller.lens.setHighlightedObject(this.quizApp.cgaApp.gfxEngine.getSceneObjectById(answer.id));
};




QuizState_QuestionLabelSelectShowAnswer.prototype.buildUI = function(panel)
{
    // Self reference
    var that = this;

    // Get current quiz questions
    var answer = this.quizApp.quizMaster.getCurrentQuestion();
    var number = this.quizApp.quizMaster.getCurrentQuestionNumber();
    var count = this.quizApp.quizMaster.getQuestionCount();

    // Produce UI content
    panel.innerHTML = "";
    panel.appendChild(DOMUtils.parse("<h3>Question " + number + " of " + count + " </h3>"));
    panel.appendChild(DOMUtils.parse("<p>The correct answers is '" + answer.name + "'.</p>"));
    var div_button = DOMUtils.div("glue");
    div_button.appendChild(DOMUtils.button("","", "Continue", function () {
        that.quizApp.queueStateChange(Quiz_Main.STATE_QUESTION_INIT);
    }));
    panel.appendChild(div_button);
};

