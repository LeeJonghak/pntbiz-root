
    elementHandler.bind({
        formBtn: 'map/group/form.do',
        regBtn: {
            action: 'submit',
            form: 'form1',
            url: 'map/group/reg.do',
            result: {
                1: {message:vm.regSuccess,
                    redirect:'map/group/list.do'},
                2: vm.regError
            }
        },
        $mformBtn: 'map/group/mform.do?groupNum=${groupNum}&page=#{page}',
        modBtn: {
            action: 'submit',
            form: 'form1',
            url: 'map/group/mod.do',
            result: {
                1: {message:vm.modSuccess,
                    redirect:'map/group/list.do?page=#{page}'},
                2: vm.modError
            }
        },
        delBtn: {
            action: 'submit',
            form: 'form1',
            url: 'map/group/del.do',
            confirm: vm.delConfirm,
            result: {
                1: {message:vm.delSuccess,
                    redirect:'map/group/list.do?page=#{page}'},
                2: vm.delError
            }
        },
        listBtn: 'map/group/list.do?page=#{page}'
    });