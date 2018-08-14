var QuizState_QuestionLabelSelect = function(quizApp)
{
    this.quizApp = quizApp;
    this.name = "Question Label Select";
};




QuizState_QuestionLabelSelect.prototype.enter = function ()
{
    var quizItem = this.quizApp.quizMaster.getCurrentQuestion();
    var sceneObj = this.quizApp.cgaApp.gfxEngine.getSceneObjectById(quizItem.id);
    this.quizApp.controller.lens.setHighlightedObject(sceneObj);
};




QuizState_QuestionLabelSelect.prototype.buildUI = function (panel)
{
    // Self reference
    var that = this;

    // Get current quiz questions
    var number = this.quizApp.quizMaster.getCurrentQuestionNumber();
    var count = this.quizApp.quizMaster.getQuestionCount();

    // Produce UI content
    panel.innerHTML = "";
    panel.appendChild(DOMUtils.parse("<h3>Question " + number + " of " + count + " </h3>"));
    panel.appendChild(DOMUtils.parse("<p>What is name of the object currently highlighted in tourquoise? You may need to dig to find it until I add a beacon highlight system, which will require some camera re-engineering</p>"));

    // Produce select element for choosing entity labels
    var div_select = DOMUtils.select(
                        "select_quiz_label",
                        this.quizApp.quizMaster.itemPool,
                        function (x) { return x.id; },
                        function (x) { return x.name; });
    panel.appendChild(div_select);

    // Store select element for access from other states
    this.quizApp.uiElements.select_label = div_select;

    // Add button
    var div_button = DOMUtils.div("glue");
    div_button.appendChild(DOMUtils.button("","", "Choose this one", function () {
        that.quizApp.queueStateChange(Quiz_Main.STATE_QUESTION_SELECT_LABEL_SUBMIT);
    }));
    panel.appendChild(div_button);
};
