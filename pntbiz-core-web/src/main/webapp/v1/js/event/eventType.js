
elementHandler.ready(function() {

});
elementHandler.bind({
    eventTypeFormBtn:'event/type/form.do',
    listBtn:'event/type/list.do',
    regBtn:{
        action: 'submit',
        form: 'form1',
        url: 'event/type/reg.do',
        result: {
            1: {message:vm.regSuccess,
                redirect:'event/type/list.do'},
            2: vm.regError
        }
    },
    modBtn: {
        action: 'submit',
        form: 'form1',
        url: 'event/type/mod.do',
        result: {
            1: {message:vm.modSuccess,
                redirect:'event/type/list.do'},
            2: vm.modError
        }
    },
    delBtn: {
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
        var source = $('#column-source').clone();
        source.find('.txt-col-id').val('');
        source.find('.txt-col-name').val('');
        source.find('.txt-col-enum').val('');
        source.find('.slct-col-type').change(function() {
            console.log(this.value);
        });
        source.find('.btn-col-remove').click(function() {
            $(this).parent().parent().remove();
        });
        $('#dynamic-column').append(source);
    }
});
