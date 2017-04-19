var producerModule = angular.module('Producer', ['ngMessages','ui.bootstrap']);

producerModule.service('ProducerModel', function() {
    var service = this,
        topic01Messages = [
            //{body: 'first topic-01 message'}
        ],
        topic02Messages = [
            //{body: 'first topic-02 message'}
        ],
        topic03Messages = [
            //{body: 'first topic-03 message'}
        ],
        topic04Messages = [
            //{body: 'first topic-04 message'}
        ];

    service.getTopic01Messages = function() {
        return topic01Messages;
    };
    service.getTopic02Messages = function() {
        return topic02Messages;
    };
    service.getTopic03Messages = function() {
        return topic03Messages;
    };
    service.getTopic04Messages = function() {
        return topic04Messages;
    };
});

producerModule.controller('MainCtrl', function(ProducerModel, $http) {
    var main = this;

    main.topic01Messages = ProducerModel.getTopic01Messages();
    main.topic02Messages = ProducerModel.getTopic02Messages();
    main.topic03Messages = ProducerModel.getTopic03Messages();
    main.topic04Messages = ProducerModel.getTopic04Messages();

    main.setTopic01Message = function(topic01Message) {
        main.topic01Message = topic01Message;
    };
    main.setTopic02Message = function(topic02Message) {
        main.topic02Message = topic02Message;
    };
    main.setTopic03Message = function(topic03Message) {
        main.topic03Message = topic03Message;
    };
    main.setTopic04Message = function(topic04Message) {
        main.topic04Message = topic04Message;
    };

    main.sendMessage = function() {
        console.log("sendMessage()");
        if (main.topic01Checkbox) {
            console.log("send message to topic-01");
            main.topic01Messages = ProducerModel.getTopic01Messages();
            main.setTopic01Message(main.uplinkMessage);
            $http.post('/rest/api/v1/messages', {topic:'topic-01', message:main.uplinkMessage});
            main.topic01Messages.push({
                body: main.uplinkMessage
            });
        }
        if (main.topic02Checkbox) {
            console.log("send message to topic-02");
            main.topic02Messages = ProducerModel.getTopic02Messages();
            main.setTopic02Message(main.uplinkMessage);
            $http.post('/rest/api/v1/messages', {topic:'topic-02', message:main.uplinkMessage});
            main.topic02Messages.push({
                body: main.uplinkMessage
            });
        }
        if (main.topic03Checkbox) {
            console.log("send message to topic-03");
            main.topic03Messages = ProducerModel.getTopic03Messages();
            main.setTopic03Message(main.uplinkMessage);
            $http.post('/rest/api/v1/messages', {topic:'topic-03', message:main.uplinkMessage});
            main.topic03Messages.push({
                body: main.uplinkMessage
            });
        }
        if (main.topic04Checkbox) {
            console.log("send message to topic-04");
            main.topic04Messages = ProducerModel.getTopic04Messages();
            main.setTopic04Message(main.uplinkMessage);
            $http.post('/rest/api/v1/messages', {topic:'topic-04', message:main.uplinkMessage});
            main.topic04Messages.push({
                body: main.uplinkMessage
            });
        }
        main.clearMessage();
    };

    main.clearMessage = function() {
        console.log("clearMessage()");
        main.uplinkMessage = "";
        main.topic01Checkbox = false;
        main.topic02Checkbox = false;
        main.topic03Checkbox = false;
        main.topic04Checkbox = false;
    }
});
