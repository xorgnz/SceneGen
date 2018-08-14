var QuizState_QuestionModelSelectIncorrect = function(quizApp)
{
    this.quizApp = quizApp;
    this.name = "Question Model Select Incorrect";
};




QuizState_QuestionModelSelectIncorrect.prototype.enter = function ()
{
    // Do Nothing
};




QuizState_QuestionModelSelectIncorrect.prototype.buildUI = function (panel)
{
    // Self reference
    var that = this;

    // Get current quiz questions
    var number = this.quizApp.quizMaster.getCurrentQuestionNumber();
    var count = this.quizApp.quizMaster.getQuestionCount();

    // Produce UI content
    panel.innerHTML = "";
    panel.appendChild(DOMUtils.parse("<h3>Question " + number + " of " + count + " </h3>"));
    panel.appendChild(DOMUtils.parse("<p>No - that's not it.</p>"));
    var div_button = DOMUtils.div("glue");
    div_button.appendChild(DOMUtils.button("","", "Show correct answer", function () {
        that.quizApp.queueStateChange(Quiz_Main.STATE_QUESTION_SELECT_MODEL_SHOW_ANSWER);
    }));
    div_button.appendChild(DOMUtils.button("","", "Continue", function () {
        that.quizApp.queueStateChange(Quiz_Main.STATE_QUESTION_INIT);
    }));
    panel.appendChild(div_button);
};
