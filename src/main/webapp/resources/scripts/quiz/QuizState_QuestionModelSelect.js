var QuizState_QuestionModelSelect = function(quizApp)
{
    this.quizApp = quizApp;
    this.name = "Question Model Select";
};




QuizState_QuestionModelSelect.prototype.enter = function ()
{
};




QuizState_QuestionModelSelect.prototype.buildUI = function (panel)
{
    // Self reference
    var that = this;

    // Get current quiz questions
    var number = this.quizApp.quizMaster.getCurrentQuestionNumber();
    var count = this.quizApp.quizMaster.getQuestionCount();
    var quizItem = this.quizApp.quizMaster.getCurrentQuestion();

    // Produce UI content
    panel.innerHTML = "";
    panel.appendChild(DOMUtils.parse("<h3>Question " + number + " of " + count + " </h3>"));
    panel.appendChild(DOMUtils.parse("<p>Which is the <b>" + quizItem.name + "</b>?</p>"));
    var div_button = DOMUtils.div("glue");
    div_button.appendChild(DOMUtils.button("","", "Choose this one", function () {
        that.quizApp.queueStateChange(Quiz_Main.STATE_QUESTION_SELECT_MODEL_SUBMIT);
    }));
    panel.appendChild(div_button);
};
