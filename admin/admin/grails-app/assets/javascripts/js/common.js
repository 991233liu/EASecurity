/*! common.js
 * ================
 * 框架js，公共的js
 *
 */
var $g = {

    /**
     * @public
     * 选中/取消，所有复选框
     */
    selectAll : function(obj) {
        if (obj.checked == true) {
            $.each($('input:checkbox[name=' + obj.id.replace('_all','') + ']').get(), function (index, it) {
                it.checked = true;
            });
        } else {
            $.each($('input:checkbox[name=' + obj.id.replace('_all','') + ']').get(), function (index, it) {
                it.checked = false;
            });
        }
    }
}