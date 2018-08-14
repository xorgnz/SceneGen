var Quiz_QuizMaster = function(itemPool)
{
    this.itemPool = _.sortBy(itemPool, "name");
    this.items = [];
    this.answers = [];
    this.index = 0;
    this.started = false;
};
Quiz_QuizMaster.DIRECTION_SELECT_MODEL = 1;
Quiz_QuizMaster.DIRECTION_SELECT_LABEL = 2;




Quiz_QuizMaster.prototype.chooseItemsByWeights = function (o, count)
{
    // Define node for our list of weighted objects. Has members:
    // - object itself
    // - object's weight
    // - total weight of object and previous objects
    var WeightedObjectNode = function (o, weight, total)
    {
        this.o = o;
        this.weight = weight;
        this.total = total;
    };

    // Create list of weighted object nodes
    var nodes = [];
    var total = 0;
    for (var i = 0 ; i < o.length ; i++)
    {
        total += o[i].p;
        nodes[i] = new WeightedObjectNode(o[i], o[i].p, total);
    }

    if (total == 0)
        throw "Cannot generate quiz - all quiz items have 0 weight";

    // Randomly select nodes from list as follows:
    // - Choose a random number between 0 .. total
    // - Select the object where obj.total > r > previous_obj.total
    // - Update selected object and later objects in list to prevent re-selection
    var result = [];
    for (var i = 0 ; i < count ; i++)
    {
        // console.log("Selection run " + i + ". Total " + total);

        // If no nodes remain with positive weights, and we're still searching,
        // rebuild the weighted list, and start again.
        if (total == 0)
        {
            for (var j = 0 ; j < o.length ; j++)
            {
                total += o[j].p;
                nodes[j].weight = o[j].p;
                nodes[j].total = total;
            }
        }

        // Select the object
        var r = Math.random() * total;
        for (var j = 0 ; j < nodes.length ; j++)
        {
            if (r < nodes[j].total)
            {
                result.push(nodes[j].o);
                break;
            }
        }

        // Update selected object weight
        var weightToRemove = nodes[j].weight;
        nodes[j].weight = 0;

        // Remove selected object's weight from following object totals
        for ( /* re-use j */ ; j < nodes.length ; j ++)
        {
            nodes[j].total -= weightToRemove;
        }

        // Remove selected object's weight from overall total
        total -= weightToRemove;
    }

    return result;
};




Quiz_QuizMaster.prototype.answer = function (answer)
{
    var question = this.getCurrentQuestion();

    var correct = false;
    if (question.direction == Quiz_QuizMaster.DIRECTION_SELECT_MODEL && question.id == answer)
        correct = true;
    else if (question.direction == Quiz_QuizMaster.DIRECTION_SELECT_LABEL && question.name == answer)
        correct = true;

    this.answers[this.index] = correct;

    return correct;
};




Quiz_QuizMaster.prototype.clear = function ()
{
    this.items = [];
    this.index = -1;
};




Quiz_QuizMaster.prototype.generateQuiz = function (size)
{
    // Validate
    if (size < 1)
        throw new Error("A quiz must have at least one element");

    // Randomly select a set of items, weighted according to the p values on each item.
    var selected = this.chooseItemsByWeights(this.itemPool, size);

    // Create questions for the selected items
    for (var i = 0 ; i < selected.length ; i++)
    {
        this.items.push(
        {
            name: selected[i].name,
            id: selected[i].id,
            factId: selected[i].factId,
            direction: Math.random() > 0.5 ? Quiz_QuizMaster.DIRECTION_SELECT_MODEL : Quiz_QuizMaster.DIRECTION_SELECT_LABEL,
        });
    }

    this.started = false;
};




Quiz_QuizMaster.prototype.getCorrectCount = function ()
{
    var count = 0;

    for (var i in this.answers)
        if (this.answers[i])
            count++;

    return count;
};



Quiz_QuizMaster.prototype.getCurrentQuestionNumber = function ()
{
    return this.index + 1;
};




Quiz_QuizMaster.prototype.getCurrentQuestion = function ()
{
    if (this.index < this.items.length)
        return this.items[this.index];
    else
    {
        console.log("Request for quiz item that doesn't exist. " + this.index);
        return null;
    }
};




Quiz_QuizMaster.prototype.getQuestionCount = function ()
{
    return this.items.length;
};




Quiz_QuizMaster.prototype.isFinished = function ()
{
    return this.index >= this.items.length;
};




Quiz_QuizMaster.prototype.nextQuestion = function ()
{
    if (! this.started)
    {
        this.started = true;
        this.index = 0;
    }
    else
        this.index ++;
};
