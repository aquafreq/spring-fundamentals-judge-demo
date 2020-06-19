document.addEventListener('DOMContentLoaded', () => {
    const textarea = document.querySelector('#textContent');
    const score = document.querySelector('#score');
    [...document.querySelectorAll('small.text-danger')]
        .forEach(s => s.style.display = 'none')


    const conditions = {
        keyup: (target) => target.value.length > 3,
        change: (target) => target.value !== ''
    };

    const activateButton = () =>
        !([...document.querySelectorAll('small.text-danger')]
            .map(x => x.style.display)
            .filter(x => x !== 'none')
            .length === 0)

    const button = document.querySelector('button[type=submit]');
    button.disabled = activateButton();

    const changeLayout = ({type, target}, conditions) => {
        const small = target.parentNode.querySelector('small').style;
        small.display = conditions[type](target) ? 'none' : 'block'
        button.disabled = activateButton();
    }

    textarea.addEventListener('keyup', (e) => changeLayout(e, conditions));
    score.addEventListener('change', (e) => changeLayout(e, conditions));
})

// $(document).ready(() => {
//     const textContent = $('#textContent');
//     const small = textContent.parent().find('small');
//     const button = $($('button[type=submit]')[0]);
//     button.attr('disabled', true);
//     let fCond = false;
//     let sCond = false;
//
//     textContent.keyup(function () {
//         fCond = this.value.length > 3;
//         fCond ? small.hide() : small.show()
//         button.attr('disabled', !(fCond && sCond));
//         console.log(fCond)
//     });
//
//     const score = $('#score');
//     const $small = $(score).parent().find('small');
//
//     score.on('change', (e) => {
//         sCond = e.target.value !== '';
//         sCond ? $small.hide() : $small.show()
//         button.attr('disabled', !(sCond && fCond));
//         console.log(sCond)
//     });
// });
