var UserPay = {
    czData: [[30, "3000屋币"], [50, "5000屋币"], [100, "10000屋币"], [200, "20000屋币"], [500, "50000屋币"], [365, "全站包年阅读"] ],
    czPayPalData: [[20, "10000屋币"], [50, "25000屋币"], [100, "50000屋币"], [80, "全站包年阅读"]],
    sendPay: function () {
        $("#payform").submit();
    }
}

$(function () {
    $("#ulPayType li").click(function () {

console.log("In pay.js, Pay amount:", $(this).attr("vals"));
        if($(this).attr("valp")==2){
//            layer.alert("微信支付暂未开通，敬请期待");
             $.ajax({
                        type: "GET",
                        url: "/contract/getAccountBalance",
                        data: {'accountAddress':inTestAddress},
                        dataType: "json",
                        success: function (data) {
                            if (data.code == 200) {
                              $("#accountBalanceOnChain").html(data.data);
            console.log("data.data:", data.data);

                            } else if(data.code == 1001){
                                //未登录
                                location.href = '/user/login.html?originUrl='+decodeURIComponent(location.href);

                            }else {
                                layer.alert(data.msg);
                            }

                        },
                        error: function () {
                            layer.alert('网络异常');
                        }
                    })
        }

        return ;

        $($(this).parent()).children().each(function () {
            $(this).removeClass("on");
        });
        $(this).addClass("on");

        var type = $(this).attr("valp");
        if (type == "3") {
            $("#ulPayPal").show();
            $("#ulPayPalXJ").show();
            $("#ulZFWX").hide();
            $("#ulZFWXXJ").hide();
        }
        else {
            $("#ulPayPal").hide();
            $("#ulPayPalXJ").hide();
            $("#ulZFWX").show();
            $("#ulZFWXXJ").show();
        }

    })

    $("#ulZFWX li").click(function () {
        $("#ulZFWX li").removeClass("on");
        $(this).addClass("on");
        if ($(this).attr("vals") > 0) {
            $("#pValue").val($(this).attr("vals"));
            $("#showTotal").html('￥' + $(this).attr("vals") + '元');
            for (var i = 0; i < UserPay.czData.length; i++) {
                if (UserPay.czData[i][0] == $(this).attr("vals")) {
                    $("#showRemark").html(UserPay.czData[i][1]);
                    break;
                }
            }
        }
    });
    $("#ulPayPal li").click(function () {
        $("#ulPayPal li").removeClass("on");
        $(this).addClass("on");
        if ($(this).attr("vals") > 0) {
            $("#pValue").val($(this).attr("vals"));
            $("#showPayPalTotal").html($(this).attr("vals") + '美元');
            for (var i = 0; i < UserPay.czData.length; i++) {
                if (UserPay.czPayPalData[i][0] == $(this).attr("vals")) {
                    $("#showPayPalRemark").html(UserPay.czPayPalData[i][1]);
                    break;
                }
            }
        }
    });
});