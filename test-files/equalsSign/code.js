let defaultParams = DCHelper.getDefaultParams({SENTV_TYPE : FILTERS.SENTV_TYPE.MOVIES});
let dc = DiscoverListDC.create({
  title      : title,
  url        : DCHelper.getUrlItem(),
  request    : VueEndpoints.Explore.items(defaultParams).updateParams(inParams)
});