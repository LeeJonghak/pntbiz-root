
elementHandler.ready(function() {

});
elementHandler.bind({
    eventTypeFormBtn:'event/type/form.do',
    listBtn1:'event/type/list.do',
    listBtn2:'event/type/list.do',
    regBtn1:{
        action: 'submit',
        form: 'form1',
        url: 'event/type/reg.do',
        result: {
            1: {message:vm.regSuccess,
                redirect:'event/type/list.do'},
            2: vm.regError
        }
    },
    regBtn2:{
        action: 'submit',
        form: 'form1',
        url: 'event/type/reg.do',
        result: {
            1: {message:vm.regSuccess,
                redirect:'event/type/list.do'},
            2: vm.regError
        }
    },
    modBtn1: {
        action: 'submit',
        form: 'form1',
        url: 'event/type/mod.do',
        result: {
            1: {message:vm.modSuccess,
                redirect:'event/type/list.do'},
            2: vm.modError
        }
    },
    modBtn2: {
        action: 'submit',
        form: 'form1',
        url: 'event/type/mod.do',
        result: {
            1: {message:vm.modSuccess,
                redirect:'event/type/list.do'},
            2: vm.modError
        }
    },
    delBtn1: {
        action: 'submit',
        form: 'form1',
        url: 'event/type/del.do',
        confirm: vm.delConfirm,
        result: {
            1: {message:vm.delSuccess,
                redirect:'event/type/list.do'},
            2: vm.delError,
            3: vm.delFail + vm.dupFail
            //3: '해당 이벤트 유형으로 등록된 이벤트가 존재합니다. 먼저, 이벤트를 삭제해 주세요.'
        }
    },
    delBtn2: {
        action: 'submit',
        form: 'form1',
        url: 'event/type/del.do',
        confirm: vm.delConfirm,
        result: {
            1: {message:vm.delSuccess,
                redirect:'event/type/list.do'},
            2: vm.delError,
            3: vm.delFail + vm.dupFail
            //3: '해당 이벤트 유형으로 등록된 이벤트가 존재합니다. 먼저, 이벤트를 삭제해 주세요.'
        }
    },
    addColumn: function() {
        var source = $('#dynamic-column').children().first().clone();
        var selectName = source.find('.selectUl').attr("data-name");
        source.find("select[name='"+selectName+"']").val(source.find('a').eq(0).attr("data-value"));
        source.find("b").text(source.find('a').eq(0).html());
        source.find("a").removeClass("selected");
        $(source.find("a").eq(0)).addClass("selected");
        source.find(".txt-col-id").val('');
        source.find(".txt-col-name").val('');
        source.find(".txt-col-enum").val('');
        source.append("<input type='button' class='btn-col-remove btn-inline btn-focus' value='삭제'>");
        source.find(".addColumn").remove();

        source.find('.btn-col-remove').click(function() {
            $(this).parent().remove();
        });
        $('#dynamic-column').append(source);
    }
});
