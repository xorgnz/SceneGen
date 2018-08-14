var QuizState_Loading = function(quizApp)
{
    this.quizApp = quizApp;
    this.name = "Loading";
};




QuizState_Loading.prototype.enter = function ()
{
    // Do nothing - this is a waiting state
};




QuizState_Loading.prototype.buildUI = function (panel)
{
    // Produce UI content
    panel.innerHTML = "";
    panel.appendChild(DOMUtils.parse("<h3>Loading...</h3>"));
    panel.appendChild(DOMUtils.p(document.createTextNode("Waiting for the scene to load..")));
};
