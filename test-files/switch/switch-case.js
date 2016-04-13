switch (this.get('reqPayload.status')) {
  case STATUS.NOT_LOADED:
    return null;
  case STATUS.LOADING:
    return 'Loading...';
  case STATUS.LOAD_SUCCESS:
    return this.get('reqPayload.value');
  case STATUS.LOAD_ERROR:
    return this.get('reqPayload.error');
  default:
    return null;
}