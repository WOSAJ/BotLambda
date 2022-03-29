function reply(content) {
    return JSC.reply(content);
}

function builder(content) {
    return JSC.builder(content);
}

function jsArr(jArr) {
    var a = new Array();
    var i;
    for(i=0;i<jArr.length;i++) {
        a[i]=jArr[i];
    }
    return a;
}