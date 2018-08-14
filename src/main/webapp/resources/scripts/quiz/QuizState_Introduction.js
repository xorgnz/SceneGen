var QuizState_Introduction = function(quizApp)
{
    this.quizApp = quizApp;
    this.name = "Introduction";
};




QuizState_Introduction.prototype.enter = function ()
{
    // Do nothing - this is a waiting state
};




QuizState_Introduction.prototype.buildUI = function (panel)
{
    // Self reference
    var that = this;

    // Produce UI content
    panel.innerHTML = "";
    panel.appendChild(DOMUtils.parse("<h3>Welcome to the anatomy quiz</h3>"));
    panel.appendChild(DOMUtils.parse("<p>This quiz will test your knowledge of anatomical structures in the <b>" + this.quizApp.config.name + "</b></p>"));
    var div_button = DOMUtils.div("glue");
    div_button.appendChild(DOMUtils.button("","", "Start a quiz", function () {
        that.quizApp.queueStateChange(Quiz_Main.STATE_QUIZ_INIT);
    }));
    panel.appendChild(div_button);
};
