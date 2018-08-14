var Quiz_Main = function(config, cgaApp)
{
    // Save linked objects
    this.cgaApp = cgaApp;
    this.config = config;
    this.mainPanel = this.config.elements.main_panel;

    // Configure controller
    this.controller = new Quiz_Controller(this);
    this.cgaApp.addController("quiz", this.controller);
    this.cgaApp.gfxEngine.meshManager.addLoadListener(this.controller);

    // Create workers
    this.communicator = new Quiz_Communicator(this.config.serverURL, this.config.studentId);
    this.quizMaster = new Quiz_QuizMaster(this.config.quizItems);

    // Create states
    this.states = {};
    this.states[Quiz_Main.STATE_INTRO] = new QuizState_Introduction(this);
    this.states[Quiz_Main.STATE_LOADING] = new QuizState_Loading(this);
    this.states[Quiz_Main.STATE_QUESTION_CORRECT] = new QuizState_QuestionCorrect(this);
    this.states[Quiz_Main.STATE_QUESTION_INIT] = new QuizState_QuestionInit(this);
    this.states[Quiz_Main.STATE_QUESTION_SELECT_LABEL] = new QuizState_QuestionLabelSelect(this);
    this.states[Quiz_Main.STATE_QUESTION_SELECT_LABEL_INCORRECT] = new QuizState_QuestionLabelSelectIncorrect(this);
    this.states[Quiz_Main.STATE_QUESTION_SELECT_LABEL_SHOW_ANSWER] = new QuizState_QuestionLabelSelectShowAnswer(this);
    this.states[Quiz_Main.STATE_QUESTION_SELECT_LABEL_SUBMIT] = new QuizState_QuestionLabelSelectSubmit(this);
    this.states[Quiz_Main.STATE_QUESTION_SELECT_MODEL] = new QuizState_QuestionModelSelect(this);
    this.states[Quiz_Main.STATE_QUESTION_SELECT_MODEL_INCORRECT] = new QuizState_QuestionModelSelectIncorrect(this);
    this.states[Quiz_Main.STATE_QUESTION_SELECT_MODEL_SHOW_ANSWER] = new QuizState_QuestionModelSelectShowAnswer(this);
    this.states[Quiz_Main.STATE_QUESTION_SELECT_MODEL_SUBMIT] = new QuizState_QuestionModelSelectSubmit(this);
    this.states[Quiz_Main.STATE_QUIZ_INIT] = new QuizState_QuizInit(this);
    this.states[Quiz_Main.STATE_SUMMARY] = new QuizState_Summary(this, config.redirectUrl);

    // Create state management variables
    this.currentState = null;
    this.semaphore_changingState = false;
    this.stateQueueHead = null;
    this.semaphore_stateQueue = false;

    // Create UI element registry
    this.uiElements = {};

    // Start quiz
    this.queueStateChange(Quiz_Main.STATE_LOADING);

    var that = this;
    this.cgaApp.gfxEngine.addSceneObjects(config.cgaObjectDescriptors, function () {
        that.cgaApp.gfxEngine.configureCameraBasic();
    });
};
Quiz_Main.STATE_INTRO = 1;
Quiz_Main.STATE_LOADING = 2;
Quiz_Main.STATE_QUESTION_CORRECT = 3;
Quiz_Main.STATE_QUESTION_INIT = 4;
Quiz_Main.STATE_QUESTION_SELECT_LABEL = 5;
Quiz_Main.STATE_QUESTION_SELECT_LABEL_INCORRECT = 6;
Quiz_Main.STATE_QUESTION_SELECT_LABEL_SHOW_ANSWER = 7;
Quiz_Main.STATE_QUESTION_SELECT_LABEL_SUBMIT = 8;

Quiz_Main.STATE_QUESTION_SELECT_MODEL = 10;
Quiz_Main.STATE_QUESTION_SELECT_MODEL_INCORRECT = 11;
Quiz_Main.STATE_QUESTION_SELECT_MODEL_SHOW_ANSWER = 12;
Quiz_Main.STATE_QUESTION_SELECT_MODEL_SUBMIT = 13;
Quiz_Main.STATE_QUIZ_INIT = 20;
Quiz_Main.STATE_SUMMARY = 21;




Quiz_Main.prototype.queueStateChange = function (state)
{
    console.log("Quiz Main - queueing state " + state);

    // Do the best job of synchronization we can.
    // Drop a state rather than fouling the works.
    if (this.semaphore_stateQueue)
        console.log("Dropping state from queue - possible synchronicity error");
    else
    {
        this.semaphore_stateQueue = true;

        // Add new state to the end of the queue
        var queueEnd = this.stateQueueHead;
        if (!queueEnd)
        {
            this.stateQueueHead = new QuizMain_QueueNode(this.states[state]);
        }
        else
        {
            while (queueEnd.next)
                queueEnd = queueEnd.next;
            queueEnd.next = new QuizMain_QueueNode(this.states[state]);
        }

        this.semaphore_stateQueue = false;

        // If not already changing start, begin changing state.
        if (! this.semaphore_changingState)
            this.moveToNextState();
    }
};




Quiz_Main.prototype.moveToNextState = function ()
{
    // Set semaphore
    this.semaphore_changingState = true;

    // Enter next state
    var queuePointer = this.stateQueueHead;
    while (queuePointer)
    {
        console.log("Quiz Main - entering state " + queuePointer.state.name);
        this.currentState = queuePointer.state;
        this.currentState.enter();
        this.currentState.buildUI(this.mainPanel);

        queuePointer = queuePointer.next;
    }

    this.stateQueueHead = null;

    // Clear semaphore
    this.semaphore_changingState = false;
};




QuizMain_QueueNode = function (state)
{
    this.state = state;
    this.next = null;
};
