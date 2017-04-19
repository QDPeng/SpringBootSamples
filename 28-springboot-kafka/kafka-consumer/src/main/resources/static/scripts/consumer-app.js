var consumerModule = angular.module('Consumer', ['ui.bootstrap']);

consumerModule.service('ConsumerModel', function() {
    var service = this,
        topic01Messages = [
            //{body: 'first topic-01 message'}
        ],
        topic02Messages = [
            //{body: 'first topic-02 message'}
        ];

    service.getTopic01Messages = function() {
        return topic01Messages;
    };
    service.getTopic02Messages = function() {
        return topic02Messages;
    };
});

consumerModule.controller('MainCtrl', function(ConsumerModel, $http) {
    var main = this;

    //main.topic01Messages = ConsumerModel.getTopic01Messages();
    //main.topic02Messages = ConsumerModel.getTopic02Messages();
    //
    //main.setTopic01Message = function(topic01Message) {
    //    main.topic01Message = topic01Message;
    //};
    //main.setTopic02Message = function(topic02Message) {
    //    main.topic02Message = topic02Message;
    //};

    var sock = new SockJS('/kafka');
    var stomp = Stomp.over(sock);

    stomp.connect('guest', 'guest', function(frame) {
        console.log('*****  Connected  *****');
        stomp.subscribe("/topic/topic-01", handleTopic01);
        //stomp.subscribe("/topic/topic-02", handleTopic02);
        testTopic01();
        //testTopic02();
    });

    function handleTopic01(message) {
        console.log('Received message on topic-01: ', message);
        console.log('message: ', message);
        console.log('messages: ', main.topic01Messages);

        main.topic01Messages = ConsumerModel.getTopic01Messages();
        //main.setTopic01Message(message.body);
        main.setTopic01Message("{body: 'first topic-01 message'}");
        main.topic01Messages.push({
            body: 'first topic-01 message'
        });

        console.log('finished received');
    }
    //function handleTopic02(message) {
    //    console.log('Received message on topic-02: ', message);
    //    main.setTopic02Message('test to topic-02');
    //    main.displayTopic02Message();
    //}

    function testTopic01() {
        console.log('Sending test to topic-01');
        stomp.send("/topic/topic-01", {}, JSON.stringify({ 'message': 'test to topic-01' }));
    }
    //function testTopic02() {
    //    console.log('Sending test to topic-02');
    //    stomp.send("/topic/topic-02", {}, JSON.stringify({ 'message': main.topic02Message }));
    //}

    main.topic01Messages = ConsumerModel.getTopic01Messages();
    //main.topic02Messages = ConsumerModel.getTopic02Messages();

    main.setTopic01Message = function(topic01Message) {
        main.topic01Message = topic01Message;
    };
    //main.setTopic02Message = function(topic02Message) {
    //    main.topic02Message = topic02Message;
    //};

    main.displayTopic01Message = function() {
        console.log('displayTopic01Message');

        main.topic01Messages = ConsumerModel.getTopic01Messages();
        main.setTopic01Message(main.topic01Message);

        main.topic01Messages.push({
            //body: main.topic01Message
            body: "blah blah"
        });

        console.log(main.topic01Message);
        console.log(main.topic01Messages);
    };
    //main.displayTopic02Message = function() {
    //    console.log('displayTopic02Message');
    //    main.topic02Messages.push({
    //        body: main.topic02Message
    //    });
    //};
});
