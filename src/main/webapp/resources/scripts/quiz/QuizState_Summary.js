var QuizState_Summary = function(quizApp, redirectUrl)
{
    this.quizApp = quizApp;
    this.redirectUrl = redirectUrl;
};




QuizState_Summary.prototype.enter = function ()
{
};




QuizState_Summary.prototype.buildUI = function (panel)
{
    // Self reference
    var that = this;

    // Get counts
    var correctCount = this.quizApp.quizMaster.getCorrectCount();
    var questionCount = this.quizApp.quizMaster.getQuestionCount();

    // Produce UI content
    panel.innerHTML = "";
    panel.appendChild(DOMUtils.parse("<h3>Answer summary</h3>"));
    panel.appendChild(DOMUtils.parse("<p>You got " + correctCount + " of " + questionCount + " correct</p>"));
    var div_button = DOMUtils.div("glue");
    div_button.appendChild(DOMUtils.button("","", "Return to student console", function () {
        location.href = that.redirectUrl;
    }));
    panel.appendChild(div_button);
};
