package org.statistic.csvloggers;

import org.statistic.models.AbstractElement;
import org.statistic.models.DmrModel;
import org.statistic.models.IRootModel;
import org.statistic.models.MBeanElement;
import org.statistic.models.MBeanModel;

@Csv
public class CsvFormatter implements ICsvFormatter<String> {
	
	@Override
	public String formatHeader(final IRootModel rootModel) {
		final StringBuffer buf = new StringBuffer();
		if (rootModel.getDmrModels() != null) {
			for (final DmrModel model : rootModel.getDmrModels()) {
				for (final AbstractElement element : model.getPathElements()) {
					formatHeaderImpl(buf, element, rootModel);
				}			
				for (final AbstractElement element : model.getChildElements()) {
					formatHeaderImpl(buf, element, rootModel);
				}
			}		
		}
		if (rootModel.getMBeanModels() != null) {
			for (final MBeanModel model : rootModel.getMBeanModels()) {
				for (final AbstractElement element : model.getMBeanElements()) {
					formatHeaderImpl(buf, element, rootModel);
				}
			}		
		}
		return buf.toString();
	}
	
	@Override
	public String formatLine(final IRootModel rootModel) {
		final StringBuffer buf = new StringBuffer();
		if (rootModel.getDmrModels() != null) {
			for (final DmrModel model : rootModel.getDmrModels()) {					
				for (final AbstractElement element : model.getPathElements()) {
					formatLineImpl(buf, element, rootModel);
				}
				for (final AbstractElement element : model.getChildElements()) {
					formatLineImpl(buf, element, rootModel);
				}
			}
		}
		if (rootModel.getMBeanModels() != null) {
			for (final MBeanModel model : rootModel.getMBeanModels()) {					
				for (final AbstractElement element : model.getMBeanElements()) {
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
				if (element.getStatisticMap().containsKey(key)) {
					FormatterUtil.add(buf, element.getStatisticMap().get(key), rootModel.getCsvSeparator());
				} else {
					FormatterUtil.add(buf, "", rootModel.getCsvSeparator());
				}
			}			
		}
	}
}
