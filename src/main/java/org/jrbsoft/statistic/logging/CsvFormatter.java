package org.jrbsoft.statistic.logging;

import org.jrbsoft.statistic.model.AbstractElement;
import org.jrbsoft.statistic.model.IProtocolModel;
import org.jrbsoft.statistic.model.IRootModel;

@Csv
public class CsvFormatter implements IFormatter<String> {

    @Override
    public String formatHeader(final IRootModel rootModel) {
        final StringBuffer buf = new StringBuffer();
        if (rootModel.getModels() != null) {
            for (final IProtocolModel model : rootModel.getModels()) {
                for (final AbstractElement element : model.getStatisticElements()) {
                    formatHeaderImpl(buf, element, rootModel);
                }
            }
        }
        return buf.toString();
    }

    @Override
    public String formatLine(final IRootModel rootModel) {
        final StringBuffer buf = new StringBuffer();
        if (rootModel.getModels() != null) {
        	for (final IProtocolModel model : rootModel.getModels()) {
                for (final AbstractElement element : model.getStatisticElements()) {
                    formatLineImpl(buf, element, rootModel);
                }
            }
        }
        return buf.toString();
    }

    //-----------------------------------------------------------------------||
    //-- Private Methods ----------------------------------------------------||
    //-----------------------------------------------------------------------||

    private void formatHeaderImpl(final StringBuffer buf, final AbstractElement element, final IRootModel rootModel) {
        if (element.getKeys() != null) {
            for (final String key : element.getKeys()) {
                FormatterUtil.add(buf, element.getAbbreviation() + "-" + key, rootModel.getCsvSeparator());
            }
        }
    }

    private void formatLineImpl(final StringBuffer buf, final AbstractElement element, final IRootModel rootModel) {
        if (element.getKeys() != null) {
            for (final String key : element.getKeys()) {
                if (element.containsStatistics(key)) {
                    FormatterUtil.add(buf, element.getCurrentValue(key), rootModel.getCsvSeparator());
                } else {
                    FormatterUtil.add(buf, "", rootModel.getCsvSeparator());
                }
            }
        }
    }
}
