import { NgModule } from '@angular/core';

import { WitcurveSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [WitcurveSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [WitcurveSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class WitcurveSharedCommonModule {}
