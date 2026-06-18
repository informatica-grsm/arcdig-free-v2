zk.afterMount(function() {
    //Upload Progress Monitor Sample 1
    zk.UPMSample1 = zk.$extends(zk.Object, {
        updated : null,
        $init : function(uplder, filenm) {
            this._uplder = uplder;
            var id = uplder.id;
            //Add message and progressmeter
            zk.Widget.$(jq("$flist")).appendChild(
                new zul.box.Hlayout({
                    id: id + "_layout",
                    spacing: "6px",
                    sclass: "UPMSample1",
                    children: [
                        new zul.wgt.Html({
                            content: ['<div class="msg"><span>', filenm,'</span></div>'].join("")
                        }),
                        new zul.wgt.Progressmeter({
                          id: id + "_pgs"
                    })]
                }) 
            );
        },
        update : function(sent, total) {
            zk.Widget.$(jq('$' + this._uplder.id + '_pgs')).setValue(sent);
        },
        destroy : function() {
            var layout = jq('$' + this._uplder.id + "_layout");
            if (zk.ie) {
                zk.Widget.$(layout).detach();
            } else {
                zk.Widget.$(layout).detach();
                //layout.animate({ width: 1}, 500, function() {
                //    zk.Widget.$(layout).detach();
               // });
            }
        }
    });
     
    //Upload Progress Monitor Sample 2
    zk.UPMSample2 = zk.$extends(zk.Object, {
        updated : null,
        $init : function(uplder, filenm) {
            this._uplder = uplder;
            var id = uplder.id,
                uri = '../widgets/effects/upload_effect/img/upload-loader.gif',
                html = ['<div id="', id,'" class="UPMSample2">',
                        '<image class="float-left" src="' , uri, '"/>',
                        '<div class="float-left">&nbsp;&nbsp;&nbsp;FileName: ', filenm , '</div>',
                        '<div class="float-right">', msgzk.FILE_SIZE,
                        '<span id="' , id, '-sent">0</span> of ',
                        '<span id="' , id, '-total">0</span>',
                        '<span id="' , id, '-percent"> (0%)</span>',
                        '</div><div class="clear"></div></div>'].join("");
            jq("$footer").append(html);
            this.viewer = jq('#' + id)[0];         
        },
        update : function(sent, total) {
            jq('#' + this._uplder.id + '-sent').html(Math.round((total / 1024) * sent / 100) + msgzk.KBYTES);
            if (!this.updated) {
                this.updated = true;
                jq('#' + this._uplder.id + '-total').html(Math.round(total / 1024) + msgzk.KBYTES);
            }
            jq('#' + this._uplder.id + '-percent').html(" (" +sent + "%" + ")");
        },
        destroy : function() {
            jq(this.viewer).slideUp(1000);
        }
    });
});