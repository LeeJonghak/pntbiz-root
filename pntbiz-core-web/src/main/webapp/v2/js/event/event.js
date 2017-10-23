
elementHandler.ready(function() {
    var filename = location.pathname.substring(location.pathname.lastIndexOf('/')+1);
    if(filename=='mform.do') {
        var evtTypeCode = $('#evtTypeCode').val();
        var evtNum = common.getQueryString('evtNum');
        var url = elementHandler.baseurl+'event/event/eventcondition.ajax.do?evtTypeCode='+evtTypeCode+'&evtNum='+evtNum;
        $('#ajax-condition').load(url);
    }

});
elementHandler.bind({
    eventFormBtn:'event/event/form.do',
    listBtn1:'event/event/list.do',
    listBtn2:'event/event/list.do',
    regBtn1:{
        action: 'submit',
        form: 'form1',
        url: 'event/event/reg.do',
        result: {
            1: {message:vm.regSuccess,
                redirect:'event/event/list.do'},
            2: vm.regError
        }
    },
    regBtn2:{
        action: 'submit',
        form: 'form1',
        url: 'event/event/reg.do',
        result: {
            1: {message:vm.regSuccess,
                redirect:'event/event/list.do'},
            2: vm.regError
        }
    },
    modBtn1: {
        action: 'submit',
        form: 'form1',
        url: 'event/event/mod.do',
        result: {
            1: {message:vm.modSuccess,
                redirect:'event/event/list.do'},
            2: vm.modError
        }
    },
    modBtn2: {
        action: 'submit',
        form: 'form1',
        url: 'event/event/mod.do',
        result: {
            1: {message:vm.modSuccess,
                redirect:'event/event/list.do'},
            2: vm.modError
        }
    },
    delBtn1: {
        action: 'submit',
        form: 'form1',
        url: 'event/event/del.do',
        confirm: vm.delConfirm,
        result: {
            1: {message:vm.delSuccess,
                redirect:'event/event/list.do'},
            2: vm.delError
        }
    },
    delBtn2: {
        action: 'submit',
        form: 'form1',
        url: 'event/event/del.do',
        confirm: vm.delConfirm,
        result: {
            1: {message:vm.delSuccess,
                redirect:'event/event/list.do'},
            2: vm.delError
        }
    },
    evtTypeCode:{
        event: 'change',
        action: function() {
            var evtTypeCode = this.value;
            var evtNum = common.getQueryString('evtNum');
            var url = elementHandler.baseurl+'event/event/eventcondition.ajax.do?evtTypeCode='+evtTypeCode;
            if(evtNum) url += '&evtNum='+evtNum;
            $('#ajax-condition').load(url);
        }
    },
    evtSearchBtn: function() {
        $('#form1').submit();
    }
});
